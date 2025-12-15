.PHONY: dev quality demo down dry-run help

help:
	@echo "Available commands:"
	@echo "  make dev       - Deploy dev-full (platform + app + databases + messaging + registry)"
	@echo "  make quality   - Deploy quality profile"
	@echo "  make demo      - Deploy demo profile"
	@echo "  make down      - Destroy dev components"
	@echo "  make dry-run   - Test dev deploy without changes"
	@echo "  make help      - Show this help message"

dev:
	helmfile apply --environment dev --selector tier=platform --selector tier=messaging

quality:
	helmfile apply --environment quality --selector tier=platform --selector tier=app --selector tier=databases --selector tier=messaging --selector tier=registry

demo:
	helmfile apply --environment demo --selector tier=platform --selector tier=databases --selector tier=registry

down:
	helmfile destroy --environment dev --selector tier=platform --selector tier=app --selector tier=databases --selector tier=messaging --selector tier=registry

dry-run:
	helmfile diff --environment dev --selector tier=platform --selector tier=app --selector tier=databases --selector tier=messaging --selector tier=registry