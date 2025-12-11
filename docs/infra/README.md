# Infrastructure Layer

## Overview
The infra module provides adapters for external concerns: data persistence, messaging, external APIs, observability. It implements ports defined in core layers.

## Responsibilities
- Repository implementations (e.g., DB access).
- Message producers/consumers (e.g., Kafka).
- External service clients.
- Metrics, logging, security adapters.

## Key Classes
- `OrderRepositoryImpl.java` (data-postgres-db1).
- Message handlers in msg-kafka.
- Clients in client-rest-app1.

## Dependencies
- Core-Domain (implements its interfaces).
- External libs: R2DBC, Kafka clients, OkHttp.

## Reactive Notes
- Use reactive drivers (e.g., R2DBC for DB).
- Messaging with reactive Kafka.

## Context for AI
- Adapters should be swappable; implement interfaces from domain.
- Handle infra concerns like retries, circuit breakers.