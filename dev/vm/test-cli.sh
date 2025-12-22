#!/bin/bash
set -euo pipefail  # Exit on error, strict mode

VM_NAME="k3s"
LOG_FILE="vm-setup.log"

log() { echo "$(date): $*" | tee -a "$LOG_FILE"; }
error() { log "ERROR: $*" >&2; exit 1; }

# Install Cilium CLI
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

# Install Linkerd CLI
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

log "CLI installations completed successfully"