.PHONY: infra compose down

infra:
	cd terraform && terraform apply

compose:
	export DOCKER_HOST=unix://$$HOME/.lima/docker/sock/docker.sock && docker-compose up -d

down:
	./scripts/destroy-cluster.sh