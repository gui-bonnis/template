#!/bin/bash
set -euo pipefail  # Exit on error, strict mode

VM_NAME="k3s"
CONFIG_FILE="vm/lima/k3s.yaml"
LOG_FILE="vm-setup.log"

log() { echo "$(date): $*" | tee -a "$LOG_FILE"; }
error() { log "ERROR: $*" >&2; exit 1; }

# Phase 1: Start VM
log "Starting Lima VM..."
limactl start "$CONFIG_FILE" || error "Failed to start VM"

# Wait for VM readiness (timeout 300s)
log "Waiting for VM to be ready..."
for i in {1..60}; do
    if limactl list | grep -q "$VM_NAME.*Running"; then
        log "VM is running"
        break
    fi
    sleep 5
done || error "VM did not start within 5 min"

# Wait for SSH and K3s (timeout 300s)
log "Waiting for K3s..."
for i in {1..60}; do
    if limactl shell "$VM_NAME" -- kubectl get nodes --kubeconfig /etc/rancher/k3s/k3s.yaml >/dev/null 2>&1; then
        log "K3s is ready"
        break
    fi
    sleep 5
done || error "K3s not ready within 5 min"

# Phase 2: Install Linkerd
log "Installing Linkerd..."
limactl shell "$VM_NAME" -- 'curl -fsL https://run.linkerd.io/install | sh' || error "Linkerd CLI install failed"
limactl shell "$VM_NAME" -- 'export PATH="$PATH:$HOME/.linkerd2/bin" && export KUBECONFIG=/etc/rancher/k3s/k3s.yaml && linkerd install --crds | kubectl apply -f -' || error "Linkerd CRDs failed"
limactl shell "$VM_NAME" -- 'export PATH="$PATH:$HOME/.linkerd2/bin" && export KUBECONFIG=/etc/rancher/k3s/k3s.yaml && linkerd install-cni | kubectl apply -f -' || error "Linkerd CNI failed"
limactl shell "$VM_NAME" -- 'export PATH="$PATH:$HOME/.linkerd2/bin" && export KUBECONFIG=/etc/rancher/k3s/k3s.yaml && linkerd install | kubectl apply -f -' || error "Linkerd control plane failed"
limactl shell "$VM_NAME" -- 'export KUBECONFIG=/etc/rancher/k3s/k3s.yaml && kubectl create namespace apps --dry-run=client -o yaml | kubectl apply -f - && kubectl annotate namespace apps linkerd.io/inject=enabled' || error "Namespace setup failed"

# Phase 3: Install dnsmasq
log "Installing dnsmasq..."
GATEWAY_IP=$(limactl shell "$VM_NAME" -- 'ip -4 addr show eth0 | grep -oP "(?<=inet\s)\d+(\.\d+){3}" | head -1' | tr -d '\r') || error "Failed to get VM IP"
log "Detected VM IP: $GATEWAY_IP"
limactl shell "$VM_NAME" -- "mkdir -p /vm/lima/dns && echo '# Only handle our local zone' > /vm/lima/dns/dnsmasq.conf && echo 'domain-needed' >> /vm/lima/dns/dnsmasq.conf && echo 'bogus-priv' >> /vm/lima/dns/dnsmasq.conf && echo 'address=/fin.soul.test/$GATEWAY_IP' >> /vm/lima/dns/dnsmasq.conf && echo 'server=1.1.1.1' >> /vm/lima/dns/dnsmasq.conf && echo 'server=8.8.8.8' >> /vm/lima/dns/dnsmasq.conf && echo 'log-queries' >> /vm/lima/dns/dnsmasq.conf && echo 'log-facility=-' >> /vm/lima/dns/dnsmasq.conf" || error "Config generation failed"
limactl shell "$VM_NAME" -- 'sudo apt update && sudo apt install -y dnsmasq' || error "dnsmasq install failed"
limactl shell "$VM_NAME" -- 'sudo cp /vm/lima/dns/dnsmasq.conf /etc/dnsmasq.conf && sudo systemctl enable dnsmasq && sudo systemctl start dnsmasq' || error "dnsmasq config/start failed"

# Phase 4: Final Validation
log "Validating setup..."
if limactl shell "$VM_NAME" -- 'export PATH="$PATH:$HOME/.linkerd2/bin" && linkerd check' | grep -q "success"; then
    log "Linkerd check passed"
else
    error "Linkerd validation failed"
fi
if limactl shell "$VM_NAME" -- 'sudo systemctl is-active dnsmasq' | grep -q "active"; then
    log "dnsmasq is active"
else
    error "dnsmasq validation failed"
fi

log "Setup complete! VM IP: $GATEWAY_IP"
log "Update macOS /etc/resolver/fin.soul.test with nameserver $GATEWAY_IP"