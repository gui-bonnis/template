resource "null_resource" "lima_docker_vm" {
  provisioner "local-exec" {
    command = <<EOF
      limactl delete ${var.vm_name} || true
      limactl start template://docker-rootful --name ${var.vm_name} --cpus ${var.cpus} --memory ${var.memory} --disk ${var.disk} || { echo "Failed to start Lima Docker VM"; exit 1; }
    EOF
  }

  triggers = {
    always_run = timestamp()
  }
}

resource "null_resource" "docker_compose" {
  depends_on = [null_resource.lima_docker_vm]

  provisioner "local-exec" {
    command = <<EOF
      mkdir -p ../html
      export DOCKER_HOST=unix://$HOME/.lima/${var.vm_name}/sock/docker.sock

    EOF
  }

  triggers = {
    always_run = timestamp()
  }
}