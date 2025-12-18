limactl shell k3s sudo cat /etc/rancher/k3s/k3s.yaml \
 | sed 's/127.0.0.1/localhost/' > ~/.kube/k3s-local.yaml
