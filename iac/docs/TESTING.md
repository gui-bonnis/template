Testing Guide for Deployed Services
Since you've successfully run make dev, here's a comprehensive plan to test and verify each service in your k3s cluster.
Run these commands after setting export KUBECONFIG=~/.kube/config-lima. Focus on health checks, connectivity, and
functionality.

1. Overall Cluster Health

- Check Nodes/Pods: kubectl get nodes (1 Ready node); kubectl get pods -A (all pods Running).
- Helm Releases: helm list -A (platform, app, databases, messaging, registry deployed).

2. Platform Services (Keycloak + PostgreSQL)

- Pods: kubectl get pods -n platform (keycloak, postgresql running).
- Keycloak:
    - Access: kubectl port-forward -n platform svc/platform-keycloak 8080:80 → Visit http://localhost:8080 (login:
      user / password from secret).
    - Logs: kubectl logs -n platform <keycloak-pod> (check for DB connection).
- PostgreSQL:
    - Connect: kubectl port-forward -n platform svc/platform-postgresql 5432:5432 → Use psql -h localhost -U bn_keycloak
      -d bitnami_keycloak (password from secret).
    - Health: kubectl exec -n platform <postgresql-pod> -- pg_isready -U bn_keycloak.

3. App Services (10 Microservices)

- Pods: kubectl get pods -n app (10 nginx-based pods running).
- Individual Service:
    - Port-forward: kubectl port-forward -n app svc/app-microservice1 8080:80 → Visit http://localhost:8080 (nginx
      welcome).
    - Logs: kubectl logs -n app <pod-name> (check for startup errors).
- Bulk Check: Loop through services: for i in {1..10}; do kubectl get svc -n app app-microservice$i; done.

4. Databases (PostgreSQL, Redis)

- Pods: kubectl get pods -n databases (postgresql, redis running).
- PostgreSQL:
    - Port-forward: kubectl port-forward -n databases svc/databases-postgresql 5432:5432.
    - Test: psql -h localhost -U postgres (password from secret).
- Redis:
    - Port-forward: kubectl port-forward -n databases svc/databases-redis 6379:6379.
    - Test: redis-cli -h localhost (password from secret); run SET test "hello" and GET test.

5. Messaging (Kafka)

- Pods: kubectl get pods -n messaging (kafka, zookeeper running).
- Kafka:
    - Port-forward: kubectl port-forward -n messaging svc/messaging-kafka 9094:9094.
    - Test: Use kafka-console-producer --broker-list localhost:9094 --topic test (in another terminal:
      kafka-console-consumer --bootstrap-server localhost:9094 --topic test --from-beginning).

6. Registry (Docker Registry)

- Pods: kubectl get pods -n registry (registry running).
- Test Push/Pull:
    - Port-forward: kubectl port-forward -n registry svc/registry 5000:5000.
    - Push: docker tag nginx localhost:5000/nginx && docker push localhost:5000/nginx.
    - Pull: docker pull localhost:5000/nginx.
      General Tips
- Secrets: Retrieve passwords with kubectl get secret -n <ns> <secret-name> -o jsonpath='{.data.<key>}' | base64 -d.
- Troubleshooting: kubectl describe pod <name> -n <ns> for events; kubectl logs <pod> -n <ns> for errors.
- Cleanup: make down to reset and redeploy if issues.

Examining Pod Status

- The user reported a pod named "platform-keycloak-0" with status "Init:ImagePullBackOff."
- This indicates the Keycloak pod is failing to pull its required image during initialization.
  Plan to Fix Keycloak ImagePullBackOff
  The ImagePullBackOff error for platform-keycloak-0 indicates the pod can't pull the Keycloak image (
  docker.io/bitnami/keycloak:25.0.4-debian-12-r1) during init. This is common in local k3s setups due to
  network/registry issues.
  Root Causes
- Image Unavailable: Tag too specific; registry rate limits or access issues.
- k3s Networking: DNS/resolution problems in VM.
- Resource Constraints: Insufficient CPU/memory for pull.
  Diagnosis Steps

1. Check Pod Details: kubectl describe pod platform-keycloak-0 -n platform (look for image pull events/errors).
2. Logs: kubectl logs platform-keycloak-0 -n platform --previous (if available).
3. k3s Status: limactl shell k3s -- docker pull docker.io/bitnami/keycloak:25.0.4-debian-12-r1 (test manual pull in VM).
4. Network: limactl shell k3s -- ping google.com (check VM internet).
   Fix Plan
1. Update Image Tag: Change to a stable tag like 25.0.4 or latest in iac/platform/values.yaml (under keycloak chart
   values).
2. Alternative Image: Switch to quay.io/keycloak/keycloak:25.0.4 if Docker Hub issues.
3. Manual Pull: Pre-pull in VM: limactl shell k3s -- docker pull <new-image>.
4. Redeploy: make down && make dev after changes.
   Tradeoffs

- Stable Tag: Less specific, but may pull older version.
- Alternative Registry: Avoids Docker Hub limits, but different image.