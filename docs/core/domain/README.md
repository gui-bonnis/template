# Core Domain Layer

## Overview

The core-domain module contains the pure business logic, aggregates, and domain services. It enforces business rules and
is independent of infrastructure.

## Responsibilities

- Define aggregates (e.g., Order).
- Implement domain services (e.g., OrderService).
- Raise domain events.
- Validate business invariants.

### Key Directories

- `aggregate/`: Domain aggregates representing business entities.
- `event/`: Domain events raised by aggregates/services.
- `exception/`: Domain-specific exceptions.
- `factory/`: Factories for creating complex aggregates or value objects.
- `mapper/`: Mappers between domain models and other layers (if needed).
- `policy/`: Business policies affecting domain behavior.
- `repository/`: Repository interfaces for aggregate persistence (no implementations here).
- `service/`: Domain services encapsulating business logic.
- `specification/`: Business rules and specifications for validation.
- `util/`: Utility classes for domain logic.
- `valueobject/`: Immutable value objects used within aggregates.

## Key Classes

- Aggregates:
- Services:
- Events:
- Exceptions: `ApplicationException.java`.

### Aggregates

#### Aggregate (i.e., Order)

- **Why**: Represent core business entities with state and behavior.
- **When**: Used to encapsulate business rules and invariants.
- **How to Use**: Interact via methods that enforce rules; raise events as needed
- **Rationale**: Central to DDD; manage consistency boundaries. See class JavaDocs for details.

#### Usage Guidelines

- Keep domain logic within aggregates and services.
- Use value objects for attributes to ensure immutability.
- Raise domain events to signal state changes.
- Enforce business rules via methods and specifications.
- Avoid direct dependencies on infrastructure; use repository interfaces for persistence.
- Write unit tests for domain logic to ensure correctness.
- Document business rules and invariants clearly in code comments.
- Use factories for complex object creation to maintain invariants.
- Leverage specifications for reusable business rules.
- Ensure domain services encapsulate operations that don't fit within a single aggregate.
- Maintain separation of concerns; domain layer should not handle technical concerns (e.g., logging, security).
- Use domain events to decouple side effects from core logic.
- Favor expressive method names that reflect business intent.
- Adopt a ubiquitous language consistent with business terminology.
- Ensure aggregates are the only source of truth for their state.
- Model complex behaviors and workflows within the domain layer.
- Utilize policies to encapsulate business decisions that may change over time.
- Promote immutability and side-effect-free functions where possible.
- Document domain events with context on when and why they are raised.
- Use repositories as abstractions for data access, keeping implementations in the infrastructure layer.
- Adopt a reactive programming model to ensure non-blocking operations within the domain logic.
- Design aggregates to be transactional boundaries, ensuring consistency within their scope.
- Leverage domain-driven design patterns to maintain a clean and maintainable codebase.
- Ensure that domain exceptions are meaningful and provide context for error handling.

#### Examples

- example 1
- example 2

#### Key Classes

#### References

- [Domain-Driven Design](https://domainlanguage.com/ddd/)
- [Reactive Programming](https://projectreactor.io/docs/core/release/reference/)
- [DDD Tactical Patterns](https://www.infoq.com/articles/ddd-tactical-patterns/)
- [Building Microservices with DDD](https://www.oreilly.com/library/view/building-microservices-with/9781491950340/)
- [Implementing Domain-Driven Design](https://www.oreilly.com/library/view/implementing-domain-driven-design/9780133039900/)
- [Domain-Driven Design Reference](https://www.infoq.com/minibooks/domain-driven-design-reference/)
- [Patterns of Enterprise Application Architecture](https://martinfowler.com/books/eaa.html)
- [Domain Events in DDD](https://martinfowler.com/eaaDev/DomainEvent.html)
- [Specification Pattern](https://martinfowler.com/apsupp/spec.pdf)
- [Value Objects in DDD](https://www.baeldung.com/java-value-objects)
- [Repository Pattern](https://martinfowler.com/eaaCatalog/repository.html)
- [Factory Pattern](https://refactoring.guru/design-patterns/factory-method)

### Events

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

### Exceptions

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

### Factories

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

### Mappers

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

### Policies

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

### Repositories

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

### Service

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

### Specifications

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

### Utils

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

### Value Objects

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

## Dependencies

- None (pure domain; no external deps).

## Reactive Notes

- Domain logic should be compatible with reactive calls (no blocking).
- Use immutable value objects.

## Context for AI

- Aggregates manage state and consistency.
- Services coordinate between aggregates.
- Follow DDD tactical patterns: entities, value objects, repositories (interfaces here).