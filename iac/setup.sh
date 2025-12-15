#!/bin/bash

# Setup script for the local dev cluster
# Runs Terraform to provision infra, then deploys the dev platform profile

set -e  # Exit on error

echo "Starting setup of local dev cluster..."

# Navigate to terraform directory
cd terraform

# Initialize and apply Terraform
echo "Provisioning infra with Terraform..."
terraform init
terraform apply -auto-approve

# Navigate back and deploy platform
cd ..
echo "Deploying dev platform profile..."
make dev

echo "Setup complete! Cluster is ready."
echo "Access: export KUBECONFIG=~/.kube/config-lima && kubectl get nodes"
echo "Deploy with Makefile (recommended):"
echo "  make dev  # Dev-full"
echo "  make quality  # Quality profile"
echo "  make demo  # Demo profile"
echo "  make platform  # Platform only"
echo "  make down  # Destroy dev components"
echo "Or use Helmfile directly for advanced control"