#!/bin/bash

# Install Helm in the VM (latest stable for arm64)
curl https://get.helm.sh/helm-v3.16.1-linux-arm64.tar.gz | tar -xz
sudo mv linux-arm64/helm /usr/local/bin/
sudo chmod +x /usr/local/bin/helm

# Set kubeconfig for k3s
export KUBECONFIG=/etc/rancher/k3s/k3s.yaml

# Add Bitnami repo (stable is deprecated)
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo update

# Deploy a sample nginx chart for testing
helm install nginx bitnami/nginx