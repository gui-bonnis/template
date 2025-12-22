#!/bin/bash
set -euo pipefail  # Exit on error, strict mode

VM_NAME="k3s"
CONFIG_FILE="lima/k3s.yaml"
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

# Phase 2: Install Cilium
log "Installing Cilium (v1.14.4)..."

limactl shell "$VM_NAME" -- bash -lc "
set -e
cd \"\$HOME\"
export KUBECONFIG=/etc/rancher/k3s/k3s.yaml

# Ensure Helm (architecture-aware, pinned v3.14.0)
ARCH=\$(uname -m)
case \"\$ARCH\" in
  x86_64) HELM_ARCH=amd64 ;;
  aarch64|arm64) HELM_ARCH=arm64 ;;
  *) echo \"Unsupported architecture: \$ARCH\"; exit 1 ;;
esac

# Ensure Helm
command -v helm >/dev/null || (
  curl -L https://get.helm.sh/helm-v3.14.0-linux-\$HELM_ARCH.tar.gz | tar zx &&
  sudo mv linux-\$HELM_ARCH/helm /usr/local/bin/helm
)

helm repo add cilium https://helm.cilium.io
helm repo update

helm upgrade --install cilium cilium/cilium \
  --version 1.14.4 \
  --namespace kube-system \
  --create-namespace \
  --wait \
  --timeout 10m \
  --set kubeProxyReplacement=partial \
  --set enablePolicy=true \
  --set k8sServiceHost=127.0.0.1 \
  --set k8sServicePort=6443 \
  --set operator.replicas=1

kubectl rollout status daemonset/cilium -n kube-system --timeout=10m
kubectl rollout status deployment/cilium-operator -n kube-system --timeout=10m

" || error "Cilium install failed"

# Phase 3: Install Linkerd
log "Installing Linkerd (edge-25.12.3)..."

limactl shell "$VM_NAME" -- bash -lc "
set -e
cd \"\$HOME\"
export KUBECONFIG=/etc/rancher/k3s/k3s.yaml

# Install Linkerd CLI (edge)
LINKERD_VERSION=edge-25.12.3
curl --proto '=https' --tlsv1.2 -sSfL https://run.linkerd.io/install-edge | sh
export PATH=\"\$HOME/.linkerd2/bin:\$PATH\"

# Install Linkerd CRDs and control plane
linkerd install --crds | kubectl apply -f -
linkerd install | kubectl apply -f -

kubectl create namespace apps --dry-run=client -o yaml | kubectl apply -f -
kubectl annotate namespace apps linkerd.io/inject=enabled

kubectl rollout status deployment/linkerd-destination -n linkerd --timeout=10m
kubectl rollout status deployment/linkerd-identity -n linkerd --timeout=10m
kubectl rollout status deployment/linkerd-proxy-injector -n linkerd --timeout=10m

" || error "Linkerd install failed"

# Install CLI
export CILIUM_CLI_VERSION=v0.15.14
export CLI_ARCH=amd64
if [ "$(uname -m)" = "aarch64" ] || [ "$(uname -m)" = "arm64" ]; then export CLI_ARCH=arm64; fi
log "installing Cilium CLI..."
if limactl shell "$VM_NAME" -- bash -lc "
export CILIUM_CLI_VERSION=\$CILIUM_CLI_VERSION
export CLI_ARCH=\$CLI_ARCH
curl -L --fail --remote-name-all https://github.com/cilium/cilium-cli/releases/download/${CILIUM_CLI_VERSION}/cilium-linux-${CLI_ARCH}.tar.gz{,.sha256sum}
sha256sum --check cilium-linux-${CLI_ARCH}.tar.gz.sha256sum
sudo tar xzvfC cilium-linux-${CLI_ARCH}.tar.gz /usr/local/bin
rm cilium-linux-${CLI_ARCH}.tar.gz{,.sha256sum}
"; then
    log "Cilium CLI validation passed"
else
    error "Cilium CLI validation failed"
fi

log "installing Linker CLI..."
if limactl shell "$VM_NAME" -- bash -lc "
export LINKERD2_VERSION=edge-25.12.3
curl --proto '=https' --tlsv1.2 -sSfL https://run.linkerd.io/install-edge | sh
export PATH=\$HOME/.linkerd2/bin:\$PATH
"; then
    log "Linkerd CLI validation passed"
else
    error "Linkerd CLI validation failed"
fi

# Phase 4: Final Validation
log "Validating setup..."
# Validate Cilium
if limactl shell "$VM_NAME" -- bash -lc "
set -e
cd \"\$HOME\"
export KUBECONFIG=/etc/rancher/k3s/k3s.yaml

kubectl rollout status daemonset/cilium -n kube-system --timeout=10m
kubectl rollout status deployment/cilium-operator -n kube-system --timeout=10m
! kubectl get pods -n kube-system | grep -q kube-proxy
! kubectl get pods -n kube-system | grep -q flannel

# Deploy test pods for connectivity check
kubectl run test-pod1 --image=busybox --restart=Never -- sleep 3600
kubectl run test-pod2 --image=busybox --restart=Never -- sleep 3600
sleep 10
kubectl exec test-pod1 -- ping -c 2 \$(kubectl get pod test-pod2 -o jsonpath='{.status.podIP}')
"; then
    log "Cilium validation passed"
else
    error "Cilium validation failed"
fi

# Validate Linkerd
if limactl shell "$VM_NAME" -- bash -lc "
set -e
cd \"\$HOME\"
export PATH=\"\$HOME/.linkerd2/bin:\$PATH\"
export KUBECONFIG=/etc/rancher/k3s/k3s.yaml

kubectl get ns linkerd
kubectl rollout status deployment/linkerd-destination -n linkerd --timeout=10m
kubectl rollout status deployment/linkerd-identity -n linkerd --timeout=10m
kubectl rollout status deployment/linkerd-proxy-injector -n linkerd --timeout=10m

# Deploy test app with Linkerd injection
kubectl create namespace test-linkerd --dry-run=client -o yaml | kubectl apply -f -
kubectl annotate namespace test-linkerd linkerd.io/inject=enabled
kubectl run test-linkerd-app --image=nginx --namespace=test-linkerd --port=80
sleep 10
kubectl get pods -n test-linkerd
"; then
    log "Linkerd validation passed"
else
    error "Linkerd validation failed"
fi

# Cleanup Linkerd test resources
limactl shell "$VM_NAME" -- bash -lc "
set -e
export KUBECONFIG=/etc/rancher/k3s/k3s.yaml

kubectl delete namespace test-linkerd --ignore-not-found=true
";

# Validate dnsmasq with timeout
TIMEOUT=30
for i in $(seq 1 $((TIMEOUT / 2))); do
    if limactl shell "$VM_NAME" -- sudo systemctl is-active dnsmasq | grep -q "active"; then
        log "dnsmasq is active"
        break
    fi
    sleep 2
done
if ! limactl shell "$VM_NAME" -- sudo systemctl is-active dnsmasq | grep -q "active"; then
    error "dnsmasq validation failed within $TIMEOUT seconds"
fi

# Final summary
limactl shell "$VM_NAME" -- bash -lc "
cd \"\$HOME\"
export KUBECONFIG=/etc/rancher/k3s/k3s.yaml
kubectl get pods -n kube-system | grep cilium
kubectl get pods -n linkerd | head -5
"