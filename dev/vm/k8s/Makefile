bootstrap:
	kubectl apply -f bootstrap/

platform:
	kubectl apply -f platform/registry
	kubectl apply -f platform/security
	kubectl apply -f platform/observability
	kubectl apply -f platform/observability/prometheus
	kubectl apply -f platform/observability/tempo
	kubectl apply -f platform/observability/grafana
	kubectl apply -f platform/apps/bff

apps:
	kubectl apply -f apps/

destroy-apps:
	kubectl delete -f apps/ --ignore-not-found

destroy-bootstrap:
	kubectl delete -f bootstrap/ --ignore-not-found

reset-k3s:
	./scripts/reset/k3s.sh && cd /k8s/ && kubectl apply -f bootstrap && kubectl apply -f platform

