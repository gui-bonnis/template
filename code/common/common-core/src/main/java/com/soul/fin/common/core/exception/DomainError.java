package com.soul.fin.common.core.exception;

public non-sealed interface DomainError extends ApplicationError {

    record EntityNotFound(String message) implements DomainError {
    }

    record InvalidInput(String message) implements DomainError {
    }
}
