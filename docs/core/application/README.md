# Core Application Layer

## Overview

The core-application module orchestrates command handling, applies cross-cutting concerns, and coordinates usecases and
repositories. It acts as the application layer in DDD.

## Responsibilities

- Handle incoming Commands via command bus.
- Execute middleware (tracing, security).
- Call usecases for business orchestration.
- Interact with repositories for queries.
- All command handlers are transactional to ensure data consistency.

### Key Directories

- `command/`: Command definitions and handlers (e.g., PlaceOrderCommand, PlaceOrderCommandHandler).
- `config/`: Application-specific configurations (e.g., CommandBus setup).
- `event/`: Domain event publishers.
- `mapper/`: Mappers between commands and domain models.
- `outbox/`: Outbox pattern implementations for reliable event publishing.
- `ports/`: Interfaces for repositories, services, messages and others.
- `ports/input/`: Input ports for commands/command handlers/messages.
- `ports/output/`: Output ports for repositories/services/messages.
- `query/`: Query definitions and handlers.
- `service/`: Application services for external services (ACL) or complex operations.
- `usecase/`: Usecase implementations (e.g., PlaceOrderUseCase).

## Commands and Handlers

Commands represent intentions for business operations, processed by handlers that orchestrate execution. Handlers are
transactional and apply middleware.

### Key Classes

- CommandBus.java: Reactive command bus interface for sending/receiving commands.

### Commands (i.e., PlaceOrderCommand)

- **Why**: Use to initiate a specific business operation (e.g., order placement), encapsulating data for creation and
  event/metrics triggering.
- **When**: For write operations requiring validation and persistence.
- **How to Use**: Send via command bus; it triggers the corresponding handler.
- **Rationale**: Separates intent from execution; enables CQRS. See class JavaDocs for data structure.

### CommandHandler (i.e., PlaceOrderCommandHandler)

- **Why**: Orchestrates command processing for a specific operation, ensuring transactional consistency and
  cross-cutting.
- **When**: Automatically invoked by bus for its command.
- **How to Use**: Delegates to usecases; handles reactive errors.
- **Rationale**: Keeps application layer focused on orchestration. See class JavaDocs for flow details.

### Usage Guidelines

- Keep handlers thin; delegate business logic to usecases.
- Apply cross-cutting via middleware chain (e.g., security, tracing).
- Use `Mono.defer` for lazy execution in handlers.
- Ensure all operations are non-blocking and reactive.
- Maintain transactional boundaries at handler level.
- Log relevant context for observability.
- Add JavaDocs for clarity on command/handler purpose.

### Examples

- **Sending Command**: From API, build a command (e.g., `PlaceOrderCommand`) and send to bus:
  `commandBus.send(command)`.
- **Middleware Application**: Handler applies security before usecase call, using `Mono.defer` for lazy execution.

### References

- CQRS: See "CQRS Journey" guide for command patterns.
- Reactive Command Bus: Check `CommandBus.java` JavaDocs for send/receive methods.
- Middleware: Refer to `SecurityMiddleware.java` for cross-cutting examples.
- Project Docs: Internal guidelines on transactional boundaries.

---

## Configurations

### Cornerstone Class (i.e., ApplicationConfig.java)

- **Why**: brief description of the class purpose.
- **When**: brief description of when to use the class.
- **How to Use**: brief description of how to use the class.
- **Rationale**: brief description of the design decisions behind the class.

### Usage Guidelines

- guideline 1
- guideline 2

### Examples

- example 1
- example 2

### Key Classes

### References

- reference 1
- reference 2

---

## Events

### Cornerstone Class (i.e., Event.java)

- **Why**: brief description of the class purpose.
- **When**: brief description of when to use the class.
- **How to Use**: brief description of how to use the class.
- **Rationale**: brief description of the design decisions behind the class.

### Usage Guidelines

- guideline 1
- guideline 2

### Examples

- example 1
- example 2

### Key Classes

### References

- reference 1
- reference 2

---

## Mappers

### Cornerstone Class (i.e., DomainMapper.java)

- **Why**: brief description of the class purpose.
- **When**: brief description of when to use the class.
- **How to Use**: brief description of how to use the class.
- **Rationale**: brief description of the design decisions behind the class.

### Usage Guidelines

- guideline 1
- guideline 2

### Examples

- example 1
- example 2

### Key Classes

### References

- reference 1
- reference 2

---

## Ports

### Input Ports

#### Cornerstone Class (i.e., xxx.java)

- **Why**: brief description of the class purpose.
- **When**: brief description of when to use the class.
- **How to Use**: brief description of how to use the class.
- **Rationale**: brief description of the design decisions behind the class.

#### Usage Guidelines

- guideline 1
- guideline 2

#### Examples

- example 1
- example 2

#### Key Classes

#### References

- reference 1
- reference 2

### Output Ports

#### Cornerstone Class (i.e., xxx.java)

- **Why**: brief description of the class purpose.
- **When**: brief description of when to use the class.
- **How to Use**: brief description of how to use the class.
- **Rationale**: brief description of the design decisions behind the class.

#### Usage Guidelines

- guideline 1
- guideline 2

#### Examples

- example 1
- example 2

#### Key Classes

#### References

- reference 1
- reference 2

---

## Queries

### QueryHandler Class (i.e., xxx.java)

- **Why**: brief description of the class purpose.
- **When**: brief description of when to use the class.
- **How to Use**: brief description of how to use the class.
- **Rationale**: brief description of the design decisions behind the class.

### Usage Guidelines

- guideline 1
- guideline 2

### Examples

- example 1
- example 2

### Key Classes

### References

- reference 1
- reference 2

---

## Services

### Cornerstone Class (i.e., xxx.java)

- **Why**: brief description of the class purpose.
- **When**: brief description of when to use the class.
- **How to Use**: brief description of how to use the class.
- **Rationale**: brief description of the design decisions behind the class.

### Usage Guidelines

- guideline 1
- guideline 2

### Examples

- example 1
- example 2

### Key Classes

### References

- reference 1
- reference 2

---

## Usecases

Usecases orchestrate domain logic for specific business operations, ensuring reactive flows and error handling. They act
as the bridge between application commands and domain services.

### Usecase (i.e: PlaceOrderUseCase)

- **Why**: Use for order creation to validate data, calculate pricing, and enforce business rules before persistence.
- **When**: Called by command handlers for write operations; ensures transactional consistency.
- **How to Use**: Pass a validated command; it returns a Mono<Order> for async processing. Handles domain events and
  metrics.
- **Rationale**: Keeps orchestration in application layer, delegating pure logic to domain. See class JavaDocs for
  parameter details and implementation flow.

### Usage Guidelines

- Usecases should be reactive and stateless.
- Use for complex business flows; simple ops can go directly in handlers.
- Integrate with repositories for data access and event publishers for decoupling.
- Handle domain exceptions and convert to application errors.
- Maintain clear method signatures for inputs/outputs.
- Log key steps for observability.
- Ensure proper error propagation in reactive chains.
- Document usecase purpose and flow in JavaDocs.

### Examples

- **Order Placement**: In `PlaceOrderCommandHandler`, call `placeOrderUseCase.execute(command)` to handle validation and
  saving. Use `flatMap` to chain with event publishing.
- **Error Handling**: Wrap usecase calls with `onErrorResume` to convert domain exceptions to application errors.

### Key Classes

### References

- DDD Patterns: See "Domain-Driven Design" by Eric Evans for usecase orchestration.
- Reactive Programming: Reactor docs at https://projectreactor.io/docs/core/release/reference/ for Mono/Flux usage.
- Code Docs: Refer to `PlaceOrderUseCase.java` JavaDocs for method signatures and flows.
- Project Wiki: Check internal docs for team-specific guidelines on transactional handlers.

---

## Dependencies

- Core-Domain (for business logic).
- Infra (for repository impls).
- Common-CommandBus (for command handling).

## Context for AI

- Keep handlers focused on orchestration, not business logic.
- Apply cross-cutting via middleware chain.
- Commands should be simple DTOs; handlers delegate to usecases.