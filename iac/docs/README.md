# IaC Setup for Local Dev Cluster

This directory contains Infrastructure as Code (IaC) for automating a local Kubernetes development cluster using Lima VM, Terraform, K3s, and Helm. The setup provisions a single-node k3s cluster with nginx pre-deployed via Helm.

## Prerequisites
- Lima VM installed (`brew install lima`)
- Terraform installed (`brew install terraform`)
- Helm installed (`brew install helm`)
- macOS (Darwin, arm64 architecture supported via Lima)

## Setup Instructions
1. Navigate to the `iac/terraform` directory.
2. Run `terraform init` to initialize the workspace.
3. Run `terraform plan` to review the changes.
4. Run `terraform apply` to provision the cluster.
   - Note: Provisioning may take 5-10 minutes due to VM boot/download. If `terraform apply` times out, run Lima commands manually (see Troubleshooting).
5. After provisioning, set the kubeconfig: `export KUBECONFIG=~/.kube/config-lima`
6. Verify the cluster: `kubectl get nodes` (should show 1 Ready node).
7. Check Helm: `helm list` (should show "nginx" deployed).

## Post-Setup Verification
1. **Set Kubeconfig**: `export KUBECONFIG=~/.kube/config-lima`
2. **Check Nodes**: `kubectl get nodes` (status: Ready).
3. **Check Pods**: `kubectl get pods` (nginx pods: Running).
4. **Check Helm Releases**: `helm list` (nginx: deployed).
5. **Access Nginx**:
   - Use port-forwarding: `kubectl port-forward svc/nginx 8080:80`
   - Visit `http://localhost:8080` in browser.
   - Note: LoadBalancer may not assign external IP in local setups; port-forwarding is recommended.
6. **Additional Checks**:
   - VM Status: `limactl list` (k3s: Running).
   - Logs: `kubectl logs <nginx-pod-name>` or `limactl shell k3s -- journalctl -u k3s`.

## Usage
- Deploy additional apps: `helm install <release-name> <chart>` (e.g., from Bitnami repo).
- Access apps: Primarily via port-forwarding (e.g., `kubectl port-forward svc/<service> <local-port>:<svc-port>`).
- Destroy cluster: Run `bash ../scripts/destroy-cluster.sh` (stops VM and cleans up).

## Troubleshooting
- **Timeout During Apply**: If `terraform apply` fails due to VM boot time, run manually:
  - `limactl delete k3s || true`
  - `limactl start template:k3s --name k3s --cpus 4 --memory 8 --disk 50`
  - Then copy kubeconfig and run setup script as in `main.tf`.
- **VM Fails to Start**: Check Lima logs: `limactl show k3s`. Ensure macOS virtualization is enabled.
- **K3s/Helm Issues**: Check VM logs: `limactl shell k3s -- journalctl -u k3s`. Verify kubeconfig path.
- **Architecture Errors**: Lima uses arm64 images; Helm binaries are arm64-compatible.
- **Resource Issues**: Ensure host has at least 4 CPUs, 8GB RAM, 50GB disk available.
- **Nginx Access**: If LoadBalancer IP not assigned, always use port-forwarding.

## Customization
- Edit `variables.tf` for VM specs (CPUs, memory, disk) or K3s version.
- Modify `setup-k3s.sh` for additional Helm repos/charts (uses arm64 Helm binary).