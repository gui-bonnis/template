#!/bin/bash

# Stop and delete the Lima VM
limactl stop k3s
limactl delete k3s

# Remove kubeconfig
rm -f ~/.kube/config-lima