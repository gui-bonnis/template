resource "null_resource" "lima_vm" {
  provisioner "local-exec" {
    command = <<EOF
      if ! command -v limactl >/dev/null 2>&1; then echo "limactl not installed"; exit 1; fi
      limactl delete ${var.vm_name} || true
      limactl start template://k3s --name ${var.vm_name} --cpus ${var.cpus} --memory ${var.memory} --disk ${var.disk} || { echo "Failed to start Lima VM"; exit 1; }
      export KUBECONFIG=$(limactl list ${var.vm_name} --format 'unix://{{.Dir}}/copied-from-guest/kubeconfig.yaml')
      cp $(limactl list ${var.vm_name} --format '{{.Dir}}/copied-from-guest/kubeconfig.yaml') ~/.kube/config-lima
      limactl copy ${path.module}/../scripts/setup-k3s.sh ${var.vm_name}:/tmp/setup-k3s.sh
      limactl shell ${var.vm_name} -- bash /tmp/setup-k3s.sh
    EOF
  }

  triggers = {
    always_run = timestamp()
  }
}