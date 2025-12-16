output "docker_socket" {
  description = "Docker socket path for the VM"
  value       = "unix://$HOME/.lima/${var.vm_name}/sock/docker.sock"
}

output "vm_name" {
  description = "Name of the created Lima VM"
  value       = var.vm_name
}