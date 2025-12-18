#!/bin/bash

set -e

echo "🔻 Stopping services..."
sudo systemctl stop k3s || true
sudo systemctl stop containerd || true

echo "🔻 Killing leftover processes..."
sudo k3s-killall.sh || true

echo "🔻 Removing K3s data..."
sudo rm -rf /var/lib/rancher/k3s
sudo rm -rf /etc/rancher/k3s

echo "🔺 Starting K3s..."
sudo systemctl start k3s

echo "🔺 Checking status..."
sudo systemctl status k3s --no-pager
echo ""
kubectl get nodes
