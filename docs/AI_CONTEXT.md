# AI Context

## Global Guidelines
- This is a reactive DDD system with Hexagonal and CQRS.
- Prefer functional programming: immutability, pure functions.
- Use reactive streams: Mono/Flux for async ops.
- Follow SOLID principles and DDD patterns.

## Coding Patterns
- Controllers: Thin, delegate to application layer.
- Handlers/Usecases: Orchestrate, apply cross-cutting.
- Domain: Pure logic, no infra deps.
- Infra: Adapters implementing domain interfaces.
- Reactive: Avoid blocking; use subscribeOn/publishOn for threads.

## Tools and Libraries
- Reactor: For reactive programming.
- Spring WebFlux: Reactive web.
- Command Bus: For CQRS command handling.
- R2DBC: Reactive DB access.
- Kafka: Reactive messaging.

## AI Augmentation Tips
- When generating code, check for reactive compatibility.
- Suggest tests with StepVerifier for reactive flows.
- Use domain events for decoupling.
- Ensure hexagonal: domain doesn't depend on infra.