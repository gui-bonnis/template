package com.soul.fin.accouting.read.advice;

import com.soul.fin.common.core.exception.ApplicationException;
import com.soul.fin.common.core.exception.EntityNotFoundException;
import com.soul.fin.common.core.exception.InvalidInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;

import java.net.URI;

@ControllerAdvice
public class ApplicationErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(ApplicationErrorHandler.class);

    @ExceptionHandler(ResourceAccessException.class)
    public ProblemDetail handle(ResourceAccessException exception) {
        log.error("resource access error", exception);
        return ProblemDetail.forStatusAndDetail(HttpStatus.SERVICE_UNAVAILABLE, exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handle(Exception exception) {
        log.error("unhandled error", exception);
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    @ExceptionHandler(ApplicationException.class)
    public ProblemDetail handleException(ApplicationException ex) {
        log.error("application error", ex);

        ProblemDetail problem;
        switch (ex.getApplicationError()) {
            case EntityNotFoundException entityNotFound:
                problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
                problem.setType(URI.create("http://example.com/problems/customer-not-found"));
                problem.setTitle("Entity Not Found");
                return problem;
            case InvalidInputException invalidInput:
                problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
                problem.setType(URI.create("http://example.com/problems/invalid-input"));
                problem.setTitle("Invalid input");
                return problem;
            default:
                return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getApplicationError().toString());
        }

    }
}
