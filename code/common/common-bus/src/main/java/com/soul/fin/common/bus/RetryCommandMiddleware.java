package com.soul.fin.common.bus;

import com.soul.fin.common.bus.core.command.Command;
import com.soul.fin.common.bus.core.command.RetryPolicy;
import com.soul.fin.common.bus.middleware.CommandMiddleware;
import com.soul.fin.common.core.exception.OptimisticConcurrencyException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Order(Ordered.HIGHEST_PRECEDENCE) // retry should be outermost
public class RetryCommandMiddleware implements CommandMiddleware {

    @Override
    public <R, C extends Command<R>>
    Function<C, Mono<R>> apply(Function<C, Mono<R>> next) {

        return command -> {

            RetryPolicy policy = command.retryPolicy();

            if (policy instanceof RetryPolicy.Optimistic(int maxRetries)) {
                return next.apply(command)
                        .retryWhen(
                                reactor.util.retry.Retry
                                        .max(maxRetries)
                                        .filter(ex -> ex instanceof OptimisticConcurrencyException)
                        );
            }

            return next.apply(command);
        };
    }
}

