# TODO

1) Add the repository to save the entities. ✅
2) Make use case transactional. ✅
3) Implement Query side for reading entities. --> Almost done, need to see query bus to flux
4) Implement tests for all layers.

- unit tests for domain and application layers.
- integration tests for API and infra layers.
- mutation tests
- architecture tests
- add reactive step validation to guarantee reactive flows.

5) Add security layer (authentication and authorization). ✅ need to review later
6) Add kafka integration for event publishing.
7) Implement OpenTelemetry tracing and metrics.
8) Add outbox pattern for reliable event delivery.
9) Add cache layer for frequently accessed data.
10) Add configuration management (e.g., Spring Cloud Config).
11) Add cross-cutting extensions

- Authentication
- Data
- Message
- Tracing
- Metrics
- Cache

12) Add other API types

- GraphQL
- WebSocket
- gRPC

13) Add Saga pattern for complex transactions
14) Implement API Patterns

- Api rate limiting
- throttling
- bulkhead
- others

15) Add dockerization and orchestration (Kubernetes)
16) Add native image support (GraalVM)
17) Add E2E tests to validate end-to-end workflows.
18) Add loading test to validate system performance under high load.
19) Implement debugging feature across all layers.
20) Add Swagger/OpenAPI documentation for all endpoints.
21) Add JavaDoc comments for all public classes and methods.
22) Set up code quality tools (e.g., SonarQube, Checkstyle).
23) Set up unified testing framework across all modules.
24) Set up unified test reporting across all modules. ✅
25) Add Creating Rest Client and Avro Schemas for external integrations using OpenAPI Generator.
26) Implement feature toggles for controlled feature releases.
27) Add data versioning and migration strategy.
28) Add Elastic Search as CQRS read model.
29) Infra full cloud native

- key cloak
- linkerd
- service mesh
-

30) Program to connect all info, datahub

8) Review architect documentation
7) Add detailed diagrams illustrating the architecture layers and data flow.
8) Add diagrams of how to implement all layers.
9) Add sequence diagrams for key usecases.
10) Add technology stack details and versions.