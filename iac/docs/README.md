# IaC Setup for Local Dev Cluster

This directory contains Infrastructure as Code (IaC) for automating a local Kubernetes development cluster using Lima VM, Terraform, K3s, Helm, and Traefik/Linkerd. It provisions a single-node k3s cluster with nginx pre-deployed, and supports multiple platform profiles (dev, quality, demo) for agile development.

## Overview
- **Infra (Terraform)**: Provisions Lima VM with k3s, installs Helm, deploys nginx.
- **Platform (Helm + Makefile)**: Deploys infra services (PostgreSQL, Redis, Keycloak, Linkerd) and placeholder microservices with profile-based toggles.
- **Profiles**: Switch between dev (all services), quality (full stack + testing), demo (minimal for showcases). DBs persist across switches.
- **Architecture**: k3s (lightweight K8s), Traefik (ingress), Linkerd (service mesh), Bitnami charts for infra.

## Prerequisites
- macOS (Darwin, arm64 architecture)
- Lima VM: `brew install lima`
- Terraform: `brew install terraform`
- Helm: `brew install helm` (host-side)
- kubectl (optional, for manual commands)

## Quick Start
Run the automated setup:
```bash
./iac/setup.sh
```
This provisions infra + deploys dev profile + registry. Takes ~10-15 min.

## Manual Setup
### 1. Provision Infra
```bash
cd iac/terraform
terraform init
terraform plan  # Review changes
terraform apply -auto-approve
```
- Provisions Lima VM with k3s.
- Installs Helm in VM, adds Bitnami repo, deploys nginx.
- Copies kubeconfig to `~/.kube/config-lima`.

### 2. Deploy Platform Profile
```bash
cd iac
export KUBECONFIG=~/.kube/config-lima
make dev  # Or make quality / make demo
```
- Deploys PostgreSQL, Redis, Keycloak, Linkerd, and enabled microservices.
- Profiles toggle services/exposure/security.

## Profiles
- **platform**: Core infra (Linkerd, Traefik, Keycloak) in `platform` ns. Always-on base.
- **dev**: All microservices (1-10) in `dev` ns. Run with `make dev`.
- **quality**: Minimal infra in `platform` ns, full Linkerd mTLS/auth, fault injection enabled. For testing.
- **demo**: Gateway-focused infra in `platform` ns, strong auth, no faults. For showcases.
- Switch: `make <profile>` (fast, ~2 min; DBs persist).

## Post-Setup Verification
1. **Set Kubeconfig**: `export KUBECONFIG=~/.kube/config-lima`
2. **Check Cluster**: `kubectl get nodes` (1 Ready node).
3. **Check Pods**: `kubectl get pods -n platform` (infra + microservices running).
4. **Check Helm**: `helm list -n platform` (platform release deployed).
5. **Access Services**:
   - Nginx: `kubectl port-forward -n platform svc/nginx 8080:80` → `http://localhost:8080`
   - Other services: Port-forward or use Traefik routes (e.g., `microservice1.localhost` via `/etc/hosts`).
6. **VM Status**: `limactl list` (k3s: Running).

## Usage
- **Makefile Commands** (Primary):
  - `make dev`: Deploy dev-full (platform + app + databases + messaging + registry).
  - `make quality`: Deploy quality profile.
  - `make demo`: Deploy demo profile.
  - `make platform`: Deploy platform only.
  - `make down`: Destroy dev components (keeps platform/DB PVCs).
  - `make dry-run`: Test dev deploy without changes.
- **Helmfile Commands** (Advanced):
  - Direct: `helmfile apply --environment dev --selector tier=platform,tier=app,tier=databases,tier=messaging,tier=registry`
- **Add Microservices**: Edit `app/values.yaml`; customize charts in `app/charts/`.
- **Access Services**: Port-forward (e.g., `kubectl port-forward -n databases svc/databases-postgresql 5432:5432`).
- **Full Teardown**: `terraform destroy` (deletes VM).

## Troubleshooting
- **Timeout During Terraform Apply**: VM boot takes time. If fails, run Lima manually:
  ```bash
  limactl delete k3s || true
  limactl start template:k3s --name k3s --cpus 4 --memory 8 --disk 50
  export KUBECONFIG=$(limactl list k3s --format 'unix://{{.Dir}}/copied-from-guest/kubeconfig.yaml')
  cp $KUBECONFIG ~/.kube/config-lima
  limactl copy iac/scripts/setup-k3s.sh k3s:/tmp/setup-k3s.sh
  limactl shell k3s -- bash /tmp/setup-k3s.sh
  ```
- **VM Issues**: Check logs: `limactl show k3s`. Ensure virtualization enabled.
- **Helm/K8s Errors**: `limactl shell k3s -- journalctl -u k3s`. Verify kubeconfig.
- **Resource Limits**: Host needs 4+ CPUs, 8GB+ RAM, 50GB+ disk.
- **Profile Fails**: Check PVCs: `kubectl get pvc -n platform`. Clean if needed.
- **Access Problems**: Use port-forwarding; Traefik handles routing.

## Customization
- **VM Specs**: Edit `terraform/variables.tf` (CPUs, memory, disk).
- **Profiles/Environments**: Modify `helmfile.yaml` environments or values files (e.g., `iac/platform/values-*.yaml`).
- **Helmfile**: Edit `helmfile.yaml` for releases/selectors.
- **Charts**: Update `iac/*/values.yaml` or `Chart.yaml` for services.
- **Microservices**: Replace placeholders in `app/charts/` with real Spring apps.
- **Scripts**: Edit `scripts/setup-k3s.sh` for additional Helm setups.

## Architecture Notes
- **Networking**: k3s Flannel; Traefik for ingress; Linkerd for mesh.
- **Security**: Linkerd mTLS; profiles add auth layers.
- **Persistence**: DBs use PVCs; survive profile switches.
- **Future**: Add OTel for observability; integrate with existing tools.

For issues, check logs or run `terraform plan` for state. Happy developing!