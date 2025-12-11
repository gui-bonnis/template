package com.soul.fin.common.core.exception;

public non-sealed interface DomainError extends ApplicationError {

    record EntityNotFound(String entity,
                          String id) implements DomainError {
    }
}
