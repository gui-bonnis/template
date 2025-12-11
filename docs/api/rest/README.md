# API Layer

## Overview

The API module handles external interactions, providing REST endpoints for commands and queries. It translates external
requests into internal Commands/Queries and delegates to the core-application layer.

## Responsibilities

- Define REST controllers for order management (e.g., place order).
- Build Command/Query objects from request DTOs.
- Handle response formatting and error handling.
- Integrate with external services if needed.

## Key Directories

- `adivice/`: Global exception handlers.
- `controller/`: REST controllers for handling HTTP requests.
- `dto/`: Data Transfer Objects for requests and responses.
- `exception/`: Custom exceptions and error handling.
- `mapper/`: Mappers between DTOs and request, response, command and query objects.
- `config/`: API-specific configurations (e.g., WebFlux, OpenAPI).

## Key Classes

- ApplicationErrorHandler: Centralized error handling for API responses.

## Usage Guidelines

- Keep controllers thin; delegate business logic to core-application.
- Validate inputs using Bean Validation (e.g., `@Valid`).
- Handle errors reactively using `onErrorResume`.
- Use WebFlux annotations (`@RestController`, `@GetMapping`, etc.) for non-blocking endpoints.
- Leverage SpringDoc OpenAPI for automatic API documentation generation.
- Use DTOs for request/response models, separate from domain models.
- Log incoming requests and outgoing responses for traceability.
- Ensure proper HTTP status codes for different outcomes (e.g., 201 for created, 400 for bad request, 500 for server
  error).
- Implement pagination and filtering for list endpoints where applicable.
- Use `Mono` and `Flux` for reactive return types in controllers.
- Apply security measures (e.g., authentication, authorization) at the API layer as needed.
- Document endpoints clearly with Swagger annotations for better developer experience.

## Examples

### Place Order Endpoint

```java@RestController
@RequestMapping("/orders")
public class OrderController {  
    private final CommandBus commandBus;


}
```

## Dependencies

- Core-Application (for command/query handling).
- Spring WebFlux (for reactive endpoints).

### References

- [Spring WebFlux Documentation](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html)
- [SpringDoc OpenAPI](https://springdoc.org/)
- [Project Reactor](https://projectreactor.io/docs/core/release/reference/)
- [Domain-Driven Design](https://domainlanguage.com/ddd/)
- [CQRS Journey](https://learn.microsoft.com/en-us/previous-versions/msp-n-p/ee817667(v=pandp.10))
- [Reactive Programming with Spring](https://spring.io/reactive)
- [API Layer Best Practices](https://www.baeldung.com/spring-boot-api-best-practices)
- [Error Handling in WebFlux](https://www.baeldung.com/spring-webflux-error-handling)
- [Bean Validation with Spring](https://www.baeldung.com/spring-boot-bean-validation)
- [Logging in Spring Boot](https://www.baeldung.com/spring-boot-logging)
- [Reactive Security in Spring](https://docs.spring.io/spring-security/reference/reactive/index.html)
- [Pagination in Spring Data WebFlux](https://www.baeldung.com/spring-data-webflux-pagination)
- [DTOs in Spring Boot](https://www.baeldung.com/spring-boot-dto-pattern)
- [Swagger Annotations](https://www.baeldung.com/swagger-2-documentation-for-spring-rest-api)
- [Reactive Controllers in Spring](https://www.baeldung.com/spring-webflux-reactive-controllers)
- [Spring Boot Exception Handling](https://www.baeldung.com/spring-boot-exception-handling)
- [Spring Boot Configuration](https://www.baeldung.com/spring-boot-configuration-properties)
- [Spring Boot Testing](https://www.baeldung.com/spring-boot-testing)
- [Reactive Streams Specification](https://www.reactive-streams.org/)
- [Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)
- [Multi-Module Maven Projects](https://maven.apache.org/guides/mini/guide-multiple-modules.html)
- [OpenTelemetry for Java](https://opentelemetry.io/docs/instrumentation/java/)

## Context for AI

- Controllers should be thin; delegate logic to core-application.
- Validate inputs early, use Bean Validation.
- Handle errors reactively with `onErrorResume`.