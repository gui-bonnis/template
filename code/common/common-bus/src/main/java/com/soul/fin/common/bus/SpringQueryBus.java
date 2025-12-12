package com.soul.fin.common.bus;

import com.soul.fin.common.bus.core.Query;
import com.soul.fin.common.bus.core.QueryBus;
import com.soul.fin.common.bus.core.QueryHandler;
import com.soul.fin.common.bus.middleware.QueryMiddleware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.function.Function;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SpringQueryBus implements QueryBus {

    private final Map<Class<?>, QueryHandler<?, ?>> handlers = new ConcurrentHashMap<>();
    private final List<QueryMiddleware> middlewares;

    public SpringQueryBus(List<QueryHandler<?, ?>> handlerBeans,
                          List<QueryMiddleware> middlewareBeans) {
        handlerBeans.forEach(handler -> {
            Class<?> cmdType = resolveQueryType(handler);
            handlers.put(cmdType, handler);
        });
        // respect Spring's @Order annotation
        middlewareBeans.sort(Comparator.comparingInt(SpringQueryBus::getOrder));
        this.middlewares = List.copyOf(middlewareBeans);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R, C extends Query<R>> Mono<R> execute(C query) {
        // Innermost execution: handler lookup and call
        Function<C, Mono<R>> execution = cmd -> {
            @SuppressWarnings("unchecked")
            var handler = (QueryHandler<C, R>) handlers.get(cmd.getClass());
            if (handler == null) {
                return Mono.error(new IllegalArgumentException("No handler for " + cmd.getClass()));
            }
            return handler.handle(cmd);
        };

        // Compose middlewares (reverse order for outermost first)
        ListIterator<QueryMiddleware> iterator = middlewares.listIterator(middlewares.size());
        while (iterator.hasPrevious()) {
            QueryMiddleware mw = iterator.previous();
            execution = mw.apply(execution);  // Wrap the execution function
        }

        // Execute the composed function
        return execution.apply(query);
    }

    private static int getOrder(QueryMiddleware mw) {
        Order order = mw.getClass().getAnnotation(Order.class);
        return order != null ? order.value() : 0;
    }

    private static Class<?> resolveQueryType(QueryHandler<?, ?> handler) {
        return Arrays.stream(handler.getClass().getGenericInterfaces())
                .filter(t -> t.getTypeName().contains("QueryHandler"))
                .findFirst()
                .map(type -> {
                    try {
                        String cmdTypeName = type.getTypeName()
                                .replaceAll(".*<([^,>]+),.*", "$1");
                        return Class.forName(cmdTypeName);
                    } catch (Exception e) {
                        throw new IllegalStateException("Cannot resolve query type for " + handler, e);
                    }
                })
                .orElseThrow(() -> new IllegalStateException("Cannot infer query type"));
    }
}
