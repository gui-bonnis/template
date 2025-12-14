output "kubeconfig_path" {
  description = "Path to the kubeconfig file for the cluster"
  value       = "~/.kube/config-lima"
}

output "vm_name" {
  description = "Name of the created Lima VM"
  value       = var.vm_name
}