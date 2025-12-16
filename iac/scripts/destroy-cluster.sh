#!/bin/bash

# Stop and delete the Lima VM
limactl stop docker
limactl delete docker

# Stop Docker Compose
export DOCKER_HOST=unix://$HOME/.lima/docker/sock/docker.sock
docker-compose down