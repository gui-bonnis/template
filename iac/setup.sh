#!/bin/bash

# Setup script for the local Docker cluster
# Runs Terraform to provision infra, then deploys Docker Compose

set -e  # Exit on error

echo "Starting setup of local Docker cluster..."

# Navigate to terraform directory
cd terraform

# Initialize and apply Terraform
echo "Provisioning infra with Terraform..."
terraform init
terraform apply -auto-approve

# Navigate back and deploy compose
cd ..
echo "Deploying Docker Compose..."
make compose

echo "Setup complete! Docker containers are running in the Lima VM."
echo "Access: export DOCKER_HOST=unix://$HOME/.lima/docker/sock/docker.sock && docker ps"
echo "Stop with: make down"