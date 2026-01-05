# TODO

1) Add the repository to save the entities. ✅
2) Make use case transactional. ✅
3) Implement Query side for reading entities. ✅

- pagination ✅
- filtering
- sorting

4) Implement tests for all layers.

- unit tests for domain and application layers.
- integration tests for API and infra layers.
- mutation tests
- architecture tests
- add reactive step validation to guarantee reactive flows.

5) Add security layer (authentication and authorization). ✅ need to review later
6) Add kafka integration for event publishing.
7) Implement OpenTelemetry tracing and metrics.
8) Add outbox pattern for reliable event delivery. ✅
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
23) Set up unified testing framework across all modules. ✅
24) Set up unified test reporting across all modules. ✅
25) Add Creating Rest Client and Avro Schemas for external integrations using OpenAPI Generator.
26) Implement feature toggles for controlled feature releases.
27) Add data versioning and migration strategy. ✅ (from event store)
28) Implement full Event Sourcing
28) Add Elastic Search as CQRS read model.
29) Infra full cloud native

- key cloak
- linkerd
- service mesh
- ci/cd
- sdlc

30) Program to connect all info, datahub

8) Review architect documentation
7) Add detailed diagrams illustrating the architecture layers and data flow.
8) Add diagrams of how to implement all layers.
9) Add sequence diagrams for key usecases.
10) Add technology stack details and versions.
11) Equalize all errors and exceptions through the application (polices, invariants, domain, events)
12) Implement Async PolicyAlert Severity and error handling
13) Implement kafka and outbox IntegrationEventDispatcher's to turn on/off using
    @Profile("outbox")
    @Component
    public class OutboxIntegrationDispatcher
    implements IntegrationEventDispatcher { ... }

    @Profile("direct-kafka")
    @Component
    public class KafkaIntegrationDispatcher
    implements IntegrationEventDispatcher { ... }

    SPRING_PROFILES_ACTIVE=outbox
14)

multiple databases connections
artifactory jfrog
argo cd
helm charts
traefik
secret vault
istio

# Nest steps

Phase 1
create projections from events✅
read projections based on event schema✅
create views and different table schemas?? ✅ (Versioning Read Models (You WILL Need This) what about 8️⃣ Versioning read
projections)
save projections at DB ✅
Adjust gets to reflect the projections created ✅
Add ACK ✅Improve ACK to be stream and reactive not polling maybe(websocket)
think of return a exception or not when timeout is thrown
test poison events

Phase 2
check rehydrate feature
Outbox module, read from table, create kafka message and send it.
consume kafka on read side
check best practices from read side, overall architecture

Phase 3
create client/server pattern to consume read on write (inside policies)
implement replay events feature
check other ES features like snapshot, etc...
review all code to be fully reactive, no blocking

observability

write tests*

review architecture components places / structure / modularity (what should be where in which way (code))

# Tech Debits

1) Create a specific module for event-store