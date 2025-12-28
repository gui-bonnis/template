package com.soul.fin.common.core.exception;


public class InvalidInputException extends ApplicationException {

    public InvalidInputException(String message) {
        super(new DomainError.InvalidInput(message));
    }

}
