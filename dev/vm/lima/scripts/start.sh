#!/usr/bin/env bash
set -e

limactl start lima/k3s.yaml
limactl shell k3s sudo kubectl get nodes
