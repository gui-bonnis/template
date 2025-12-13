package com.soul.fin.common.bus;

import com.soul.fin.common.bus.core.Command;
import com.soul.fin.common.bus.middleware.CommandMiddleware;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Function;

@Component
public final class LogCommandMiddleware implements CommandMiddleware {
    @Override
    public <R, C extends Command<R>> Function<C, Mono<R>> apply(Function<C, Mono<R>> next) {
        return cmd -> {
            String traceId = UUID.randomUUID().toString();
            long start = System.nanoTime();
            return next.apply(cmd)
                    .contextWrite(ctx -> ctx.put("TRACE", traceId))
                    .doOnSubscribe(s -> log("start", traceId, cmd))
                    .doOnSuccess(r -> log("success", traceId, cmd, r))
                    .doOnError(e -> log("error", traceId, cmd, e))
                    .doFinally(signal -> {
                        long duration = System.nanoTime() - start;
                        System.out.printf("[trace=%s] finished in %d ns with signal %s%n", traceId, duration, signal);
                    });
        };
    }

    private <C extends Command<?>> void log(String event, String traceId, C command) {
        System.out.printf("[trace=%s] %s %s%n", traceId, event, command.getClass().getSimpleName());
    }

    private <C extends Command<?>, R> void log(String event, String traceId, C command, R result) {
        System.out.printf("[trace=%s] %s %s with result %s%n", traceId, event, command.getClass().getSimpleName(), result);
    }

    private <C extends Command<?>> void log(String event, String traceId, C command, Throwable error) {
        System.out.printf("[trace=%s] %s %s %s%n", traceId, event, command.getClass().getSimpleName(), error);
    }
}
