start:
	./scripts/start.sh

stop:
	./scripts/stop.sh

destroy:
	./scripts/destroy.sh

kubeconfig:
	./scripts/kubeconfig.sh

platform:
	kubectl apply -f k8s/bootstrap
	kubectl apply -f k8s/platform/registry
	kubectl apply -f k8s/platform/security
	kubectl apply -f k8s/platform/observability
	kubectl apply -f k8s/platform/observability/prometheus
	kubectl apply -f k8s/platform/observability/tempo
	kubectl apply -f k8s/platform/observability/grafana
	kubectl apply -f k8s/platform/apps
