variable "vm_name" {
  description = "Name of the Lima VM"
  type        = string
  default     = "docker"
}

variable "cpus" {
  description = "Number of CPUs for the VM"
  type        = number
  default     = 4
}

variable "memory" {
  description = "Memory in GiB for the VM"
  type        = string
  default     = "8"
}

variable "disk" {
  description = "Disk size in GiB for the VM"
  type        = string
  default     = "50"
}