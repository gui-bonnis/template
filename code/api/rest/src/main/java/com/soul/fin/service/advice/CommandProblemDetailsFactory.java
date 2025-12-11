package com.soul.fin.service.advice;


import org.springframework.stereotype.Component;

@Component
public class CommandProblemDetailsFactory {
//
//    public ProblemDetail fromRejected(CommandResult.ValidationError validationError) {
//        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
//        pd.setTitle("Domain rule violated");
//        pd.setDetail(validationError.violations().toString());

//        pd.setProperty("errorCode", validationError.errorCode());
//
//        // create stable URIs for machine-readable error types
//        pd.setType(URI.create("https://api.yourapp.com/errors/"
//                + validationError.errorCode().toLowerCase()));

    //pd.setProperty("correlationId", MDC.get("correlationId"));
    //pd.setProperty("traceId", tracer.currentSpan().context().traceId());

//        return pd;
//    }

//    public ProblemDetail fromFailure(CommandResult.CommandFailure failure) {
//        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
//        pd.setTitle("Unexpected system error");
//        pd.setDetail(failure.message());
//
//        if (failure.exception() != null) {
//            pd.setProperty("exception", failure.exception().getClass().getSimpleName());
//        }
//
//        pd.setType(URI.create("https://api.yourapp.com/errors/technical-failure"));
//
//        //pd.setProperty("correlationId", MDC.get("correlationId"));
//        //pd.setProperty("traceId", tracer.currentSpan().context().traceId());
//
//        return pd;
//    }
}

