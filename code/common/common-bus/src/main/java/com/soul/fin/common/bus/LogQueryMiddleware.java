package com.soul.fin.common.bus;

import com.soul.fin.common.bus.core.QueryMany;
import com.soul.fin.common.bus.core.QueryOne;
import com.soul.fin.common.bus.middleware.QueryMiddleware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Function;

@Order(20)
@Component
public final class LogQueryMiddleware implements QueryMiddleware {
    @Override
    public <R, Q extends QueryOne<R>> Function<Q, Mono<R>> applyOne(Function<Q, Mono<R>> next) {
        return qry -> {
            String traceId = UUID.randomUUID().toString();
            long start = System.nanoTime();
            return next.apply(qry)
                    .contextWrite(ctx -> ctx.put("TRACE", traceId))
                    .doOnSubscribe(s -> log("start", traceId, qry))
                    .doOnSuccess(r -> log("success", traceId, qry, r))
                    .doOnError(e -> log("error", traceId, qry, e))
                    .doFinally(signal -> {
                        long duration = System.nanoTime() - start;
                        System.out.printf("[trace=%s] finished in %d ns with signal %s%n", traceId, duration, signal);
                    });
        };
    }

    private <Q extends QueryOne<?>> void log(String event, String traceId, Q qry) {
        System.out.printf("[trace=%s] %s %s%n", traceId, event, qry.getClass().getSimpleName());
    }

    private <Q extends QueryOne<?>, R> void log(String event, String traceId, Q qry, R result) {
        System.out.printf("[trace=%s] %s %s with result %s%n", traceId, event, qry.getClass().getSimpleName(), result);
    }

    private <Q extends QueryOne<?>> void log(String event, String traceId, Q qry, Throwable error) {
        System.out.printf("[trace=%s] %s %s %s%n", traceId, event, qry.getClass().getSimpleName(), error);
    }


    @Override
    public <R, Q extends QueryMany<R>> Function<Q, Flux<R>> applyMany(Function<Q, Flux<R>> next) {
        return qry -> {
            String traceId = UUID.randomUUID().toString();
            long start = System.nanoTime();
            return next.apply(qry)
                    .contextWrite(ctx -> ctx.put("TRACE", traceId))
                    .doOnSubscribe(s -> log("start", traceId, qry))
                    .doOnError(e -> log("error", traceId, qry, e))
                    .doFinally(signal -> {
                        long duration = System.nanoTime() - start;
                        System.out.printf("[trace=%s] finished in %d ns with signal %s%n", traceId, duration, signal);
                    });
        };
    }

    private <Q extends QueryMany<?>> void log(String event, String traceId, Q qry) {
        System.out.printf("[trace=%s] %s %s%n", traceId, event, qry.getClass().getSimpleName());
    }

    private <Q extends QueryMany<?>> void log(String event, String traceId, Q qry, Throwable error) {
        System.out.printf("[trace=%s] %s %s %s%n", traceId, event, qry.getClass().getSimpleName(), error);
    }
}
