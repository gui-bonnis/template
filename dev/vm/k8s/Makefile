bootstrap:
	kubectl apply -f bootstrap/

platform:
	kubectl apply -f platform/

apps:
	kubectl apply -f apps/

destroy-apps:
	kubectl delete -f apps/ --ignore-not-found

destroy-bootstrap:
	kubectl delete -f bootstrap/ --ignore-not-found

reset-k3s:
	./scripts/reset/k3s.sh && cd /k8s/ && kubectl apply -f bootstrap && kubectl apply -f platform