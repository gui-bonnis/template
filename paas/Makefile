PROFILE ?= demo
ENV ?= dev

MODULES := $(shell yq '.modules[]' profiles/$(PROFILE).yml)

COMPOSE_FILES := $(foreach m,$(MODULES),-f modules/$(m)/compose.yml)

up:
	export DOCKER_HOST=unix://$$HOME/.lima/docker/sock/docker.sock && docker compose $(COMPOSE_FILES) --profile $(PROFILE) up -d

down:
	export DOCKER_HOST=unix://$$HOME/.lima/docker/sock/docker.sock && docker compose $(COMPOSE_FILES) --profile $(PROFILE) down

ps:
	export DOCKER_HOST=unix://$$HOME/.lima/docker/sock/docker.sock && docker compose $(COMPOSE_FILES) ps

#make exec SERVICE=00-runtime-kafka-1
exec:
	export DOCKER_HOST=unix://$$HOME/.lima/docker/sock/docker.sock && docker exec -it $(SERVICE) bash

clean-volume:
	export DOCKER_HOST=unix://$$HOME/.lima/docker/sock/docker.sock && docker volume prune -f