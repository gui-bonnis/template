status:
	kubectl get nodes && \
    kubectl get pods -n platform && \
    kubectl get pods -n data && \
    kubectl get pods -n security

start-cluster:
	kubectl apply -f bootstrap/ && \
	kubectl apply -f platform/security  && \
	kubectl apply -f platform/registry  && \
    kubectl apply -f platform/ingress/coredns-custom-rewrites.yaml  && \
	kubectl apply -f platform/ingress  && \
	kubectl apply -f platform/observability  && \
	kubectl apply -f platform/observability/prometheus  && \
	kubectl apply -f platform/observability/tempo  && \
	kubectl apply -f platform/observability/grafana && \
	kubectl apply -f platform/data/postgres/namespace.yaml  && \
	kubectl apply -f platform/data/postgres/secret.yaml  && \
    kubectl apply -f platform/data/postgres/service.yaml  && \
	kubectl apply -f platform/data/postgres/statefulset.yaml  && \
    kubectl apply -f overlays/demo/postgres-nodeport.yaml  && \
	kubectl apply -f platform/messaging/kafka/namespace.yaml  && \
	kubectl apply -f platform/messaging/kafka/configmap.yaml  && \
	kubectl apply -f platform/messaging/kafka/secret.yaml  && \
    kubectl apply -f platform/messaging/kafka/service.yaml  && \
	kubectl apply -f platform/messaging/kafka/statefulset.yaml  && \
    kubectl apply -f overlays/demo/kafka-nodeport.yaml  && \
    kubectl apply -f platform/data/postgres/networkpolicy.yaml  && \
    kubectl apply -f platform/messaging/kafka/networkpolicy.yaml  && \

	kubectl apply -f apps/bff

bootstrap:
	kubectl apply -f bootstrap/

platform:
	kubectl apply -f platform/security  && \
	kubectl apply -f platform/registry  && \
	kubectl apply -f platform/observability  && \
	kubectl apply -f platform/observability/prometheus  && \
	kubectl apply -f platform/observability/tempo  && \
	kubectl apply -f platform/observability/grafana  && \

apps:
	kubectl apply -f apps/

destroy-apps:
	kubectl delete -f apps/ --ignore-not-found

destroy-bootstrap:
	kubectl delete -f bootstrap/ --ignore-not-found

destroy-platform:
	kubectl delete -f platform/ --ignore-not-found

reset-k3s:
	./scripts/reset/k3s.sh && cd /k8s/ && \
    kubectl apply -f bootstrap && \
    kubectl apply -f platform

