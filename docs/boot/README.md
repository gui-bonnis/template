# Boot Layer

## Overview
The boot module assembles the entire application, handles configuration, and provides the entry point.

## Responsibilities
- Define main application class.
- Configure beans and dependencies.
- Set up Spring context.
- Handle startup and shutdown.

## Key Classes
- `Application.java`: Main class with @SpringBootApplication.
- `BeanConfiguration.java`: Additional configs.

## Dependencies
- All other modules (api, core-application, infra).

## Reactive Notes
- Uses WebFlux for reactive web server.
- Configures reactive command bus.

## Context for AI
- Keep boot minimal; delegate to other layers.
- Use profiles for different environments.