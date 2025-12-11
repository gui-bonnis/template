package com.soul.fin.common.bus;

import com.soul.fin.common.bus.core.Command;
import com.soul.fin.common.bus.middleware.CommandMiddleware;
import com.soul.fin.common.bus.middleware.NextCommand;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public final class SimpleTracingMiddleware implements CommandMiddleware {
    @Override
    public <R, C extends Command<R>> Mono<C> invoke(C command, NextCommand next) {
        String traceId = UUID.randomUUID().toString();
        long start = System.nanoTime();
        return next.invoke(command)
                .contextWrite(ctx -> ctx.put("TRACE", traceId))
                .doOnSubscribe(s -> log("start", traceId, command))
                .doOnSuccess(r -> log("success", traceId, command))
                .doOnError(e -> log("error", traceId, command, e))
                .doFinally(signal -> {
                    long duration = System.nanoTime() - start;
                    System.out.printf("[trace=%s] finished in %d ns with signal %s%n", traceId, duration, signal);
                });

    }

    private <C extends Command<?>> void log(String event, String traceId, C command) {
        System.out.printf("[trace=%s] %s %s%n", traceId, event, command.getClass().getSimpleName());
    }

    private <C extends Command<?>> void log(String event, String traceId, C command, Throwable error) {
        System.out.printf("[trace=%s] %s %s %s%n", traceId, event, command.getClass().getSimpleName(), error);
    }
}
