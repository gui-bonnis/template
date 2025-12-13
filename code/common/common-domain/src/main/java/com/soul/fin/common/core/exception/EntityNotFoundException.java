package com.soul.fin.common.core.exception;


public class EntityNotFoundException extends ApplicationException {

    public EntityNotFoundException(String message) {
        super(new DomainError.EntityNotFound(message));
    }

}
