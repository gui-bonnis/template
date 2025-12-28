package com.soul.fin.common.core.exception;

public class ApplicationExceptions {

    public static <T> T remoteServiceError(String service, String message) {
        var error = new SystemError.RemoteServiceError(service, message);
        throw new ApplicationException(error);
    }

    public static <T> T eventAlreadyProcessed(String service, String message) {
        var error = new SystemError.EventAlreadyProcessed(service, message);
        throw new ApplicationException(error);
    }

}
