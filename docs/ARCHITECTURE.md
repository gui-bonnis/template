# System Architecture

## Overview
This project implements a multi-module Java solution based on Domain-Driven Design (DDD), Hexagonal Architecture, and Command Query Responsibility Segregation (CQRS). The system is fully reactive, using Project Reactor and Spring WebFlux for asynchronous processing.

## Layers
- **API**: External interfaces for receiving and returning data. Builds commands and queries, delegates to core-application.
- **Core-Application**: Handles commands, executes cross-cutting concerns (e.g., logging, security), orchestrates usecases and repositories.
- **Core-Domain**: Contains business logic, aggregates, and domain services enforcing rules.
- **Infra**: Infrastructure adapters for data access, messaging, external services, metrics, and logs.
- **Boot**: Application assembly, configuration, and startup.

## Data Flow
1. API receives requests, builds Commands/Queries.
2. Core-Application processes them, applies cross-cutting, calls Core-Domain services.
3. Core-Domain executes business logic, raises events.
4. Infra handles persistence, messaging, etc.
5. Responses flow back through the layers.

## Reactive Design
- All layers use reactive streams (Mono/Flux).
- Asynchronous and non-blocking for scalability.
- Technologies: Spring Boot 3.x, Reactor, WebFlux, R2DBC (for DB), Kafka (messaging).

## Technologies
- Java 25
- Spring Boot 3.5.x
- Maven (multi-module)
- PostgreSQL (data)
- Kafka (messaging)
- OpenTelemetry (observability)

## Context for AI
- Prefer functional programming and immutability.
- Use reactive patterns: avoid blocking calls.
- Follow DDD: aggregates as consistency boundaries.
- Hexagonal: domain core isolated from infra.