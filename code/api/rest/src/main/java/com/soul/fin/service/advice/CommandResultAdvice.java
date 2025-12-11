package com.soul.fin.service.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class CommandResultAdvice {

    private final CommandProblemDetailsFactory problemDetails;

    public CommandResultAdvice(CommandProblemDetailsFactory problemDetails) {
        this.problemDetails = problemDetails;
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<?> handleThrowable(Throwable ex) {
        ex.printStackTrace(); // you will replace with structured logging
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Unexpected error"));
    }

//    @ResponseBody
//    @ExceptionHandler(CommandResult.class)
//    public Mono<ResponseEntity<?>> handleCommandResult(CommandResult result) {
//
//        if (result instanceof CommandResult.CommandSuccess success) {
//            return Mono.just(ResponseEntity.ok(success));
//        }
//
//        if (result instanceof CommandResult.CommandAccepted accepted) {
//            return Mono.just(ResponseEntity.accepted().body(accepted));
//        }
//
//        if (result instanceof CommandResult.CommandRejected rejected) {
//            ProblemDetail pd = problemDetails.fromRejected(rejected);
//            return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd));
//        }
//
//        if (result instanceof CommandResult.CommandFailure failure) {
//            ProblemDetail pd = problemDetails.fromFailure(failure);
//            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pd));
//        }
//
//        // fallback for unexpected types
//        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
//                HttpStatus.INTERNAL_SERVER_ERROR,
//                "Unknown CommandResult type"
//        );
//        return Mono.just(ResponseEntity.internalServerError().body(pd));
//    }
}
