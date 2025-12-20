# K3s Development Environment with Lima

This project sets up an isolated Kubernetes development environment using Lima (a Linux VM manager) and K3s (a
lightweight Kubernetes distribution). The setup provides a sandbox for deploying and testing Kubernetes services without
affecting the host machine.

## Project Structure

- **box/**: Contains devbox configurations to isolate host machines from CLI tools and installations.
- **vm/lima/**: Holds the Lima configuration file (`lima.yaml`) to create and manage a Linux VM running K3s.
- **vm/k8s/**: Stores Infrastructure as Code (k8s) configurations (e.g., YAML manifests, Terraform files) used to set up
  Kubernetes services in the K3s cluster.

## How It Works

1. **VM Creation**: Lima launches an Ubuntu-based VM using the configuration in `vm/lima/lima.yaml`.
2. **K3s Installation**: The VM automatically installs K3s, a certified Kubernetes distribution optimized for
   resource-constrained environments.
3. **Service Deployment**: k8s configurations in `vm/k8s/` are applied to deploy and manage Kubernetes services (e.g.,
   pods, services, persistent volumes).
4. **PVC Storage**: Persistent Volume Claims (PVCs) use K3s's built-in `local-path` storage class, storing data in
   `/var/lib/rancher/k3s/storage/` inside the VM.
5. **Access and Management**: Access the VM shell with `limactl shell lima`, and manage Kubernetes resources via
   `kubectl` using the exported kubeconfig.

## Requirements

- Install Lima: Follow [Lima's installation guide](https://github.com/lima-vm/lima#installation) (e.g.,
  `brew install lima` on macOS).

## Setup Instructions

1. **Clone or navigate to this repository**.
2. **Start the Lima VM**:
   ```
   limactl start vm/lima/lima.yaml
   ```
   This downloads the Ubuntu image, sets up the VM, installs K3s, and mounts the k8s folder.
3. **Access the VM**:
   ```
   limactl shell lima
   ```
   The k8s folder is available at `/k8s` inside the VM (writable).
4. **Use kubectl on the host**:
   ```
   export KUBECONFIG="/Users/gui/.lima/lima/copied-from-guest/kubeconfig.yaml"
   kubectl get nodes
   ```
5. **Deploy Services**: Apply k8s configs from `vm/k8s/` using `kubectl apply -f <file.yaml>` or k8s resources.
6. **PVC Example**: Create a PVC with the `local-path` storage class for persistent storage.

## Next Steps

- Populate `vm/k8s/` with your Kubernetes manifests or k8s scripts.
- Customize `vm/lima/lima.yaml` for additional mounts, resources, or K3s arguments.
- Stop the VM with `limactl stop lima` when done.

## Linker setup

Step 2 — Fix server address (important!)

Edit the file:

vim ~/.kube/config

Find this line:

server: https://127.0.0.1:6443

Change it to:

server: https://localhost:6443

Save and exit.

On MacOs
dnsmasq wildcard config

Create config file:

sudo nano /etc/dnsmasq.d/fin-soul.conf

Contents (exact):

address=/fin.soul.test/127.0.0.1

Purpose:

Any *.fin.soul.test resolves to localhost

Matches Lima networking model

#TODO
Verify NetworkPolicy blocks unauthorized namespaces (postgres phase)