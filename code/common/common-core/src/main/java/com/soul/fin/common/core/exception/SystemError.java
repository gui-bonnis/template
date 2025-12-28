package com.soul.fin.common.core.exception;

public non-sealed interface SystemError extends ApplicationError {

    record RemoteServiceError(String service,
                              String message) implements SystemError {

    }

    record EventAlreadyProcessed(String service,
                                 String message) implements SystemError {

    }
}
