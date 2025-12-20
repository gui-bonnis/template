.PHONY: setup validate clean

setup:
	./vm/setup-vm.sh

validate:
	@echo "Running validations..."
	@if limactl list | grep -q "k3s.*Running"; then \
		echo "VM is running"; \
	else \
		echo "VM not running"; exit 1; \
	fi
	@if limactl shell k3s -- bash -lc 'export KUBECONFIG=/etc/rancher/k3s/k3s.yaml && kubectl get nodes' >/dev/null 2>&1; then \
		echo "K3s is ready"; \
	else \
		echo "K3s not ready"; exit 1; \
	fi
	@if limactl shell k3s -- bash -lc 'export KUBECONFIG=/etc/rancher/k3s/k3s.yaml && kubectl get pods -n kube-system | grep -q cilium'; then \
		echo "Cilium is running"; \
	else \
		echo "Cilium not running"; exit 1; \
	fi
	@if limactl shell k3s -- bash -lc 'export KUBECONFIG=/etc/rancher/k3s/k3s.yaml && kubectl get pods -n linkerd | grep -q Running'; then \
		echo "Linkerd is running"; \
	else \
		echo "Linkerd not running"; exit 1; \
	fi
	@if limactl shell k3s -- sudo systemctl is-active dnsmasq | grep -q "active"; then \
		echo "dnsmasq is active"; \
	else \
		echo "dnsmasq not active"; exit 1; \
	fi
	@echo "All validations passed!"

clean:
	limactl stop k3s && limactl delete k3s