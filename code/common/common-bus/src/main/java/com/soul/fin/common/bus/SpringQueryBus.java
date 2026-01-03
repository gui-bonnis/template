package com.soul.fin.common.bus;

import com.soul.fin.common.bus.core.query.*;
import com.soul.fin.common.bus.middleware.QueryMiddleware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Component
public class SpringQueryBus implements QueryBus {

    private final Map<Class<?>, QueryOneHandler<?, ?>> oneHandlers = new ConcurrentHashMap<>();
    private final Map<Class<?>, QueryManyHandler<?, ?>> manyHandlers = new ConcurrentHashMap<>();

    //    private final Map<Class<?>, QueryHandler<?, ?>> handlers = new ConcurrentHashMap<>();
    private final List<QueryMiddleware> middlewares;

    public SpringQueryBus(List<QueryOneHandler<?, ?>> oneHandlerBeans,
                          List<QueryManyHandler<?, ?>> manyHandlerBeans,
                          List<QueryMiddleware> middlewareBeans) {
        oneHandlerBeans.forEach(handler -> {
            Class<?> cmdType = resolveQueryType(handler);
            oneHandlers.put(cmdType, handler);
        });

        manyHandlerBeans.forEach(handler -> {
            Class<?> cmdType = resolveQueryType(handler);
            manyHandlers.put(cmdType, handler);
        });

        // respect Spring's @Order annotation
        middlewareBeans.sort(Comparator.comparingInt(SpringQueryBus::getOrder));
        this.middlewares = List.copyOf(middlewareBeans);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R, Q extends QueryOne<R>> Mono<R> ask(Q query) {
        Function<Q, Mono<R>> execution = cmd -> {
            var handler = (QueryOneHandler<Q, R>) oneHandlers.get(query.getClass());

            if (handler == null) {
                return Mono.error(
                        new IllegalStateException("No handler for query " + query.getClass())
                );
            }
            return handler.handle(cmd);
        };

        // Compose middlewares (reverse order for outermost first)
        ListIterator<QueryMiddleware> iterator = middlewares.listIterator(middlewares.size());
        while (iterator.hasPrevious()) {
            QueryMiddleware mw = iterator.previous();
            execution = mw.applyOne(execution);  // Wrap the execution function
        }

        // Execute the composed function
        return execution.apply(query);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R, Q extends QueryMany<R>> Flux<R> askMany(Q query) {
        Function<Q, Flux<R>> execution = cmd -> {
            var handler = (QueryManyHandler<Q, R>) manyHandlers.get(query.getClass());

            if (handler == null) {
                return Flux.error(
                        new IllegalStateException("No handler for query " + query.getClass())
                );
            }
            return handler.handle(cmd);
        };

        // Compose middlewares (reverse order for outermost first)
        ListIterator<QueryMiddleware> iterator = middlewares.listIterator(middlewares.size());
        while (iterator.hasPrevious()) {
            QueryMiddleware mw = iterator.previous();
            execution = mw.applyMany(execution);  // Wrap the execution function
        }

        // Execute the composed function
        return execution.apply(query);
    }


    private static int getOrder(QueryMiddleware mw) {
        Order order = mw.getClass().getAnnotation(Order.class);
        return order != null ? order.value() : 0;
    }

    private static Class<?> resolveQueryType(QueryManyHandler<?, ?> handler) {
        return resolveQueryType(handler.getClass(), "QueryManyHandler");
    }

    private static Class<?> resolveQueryType(QueryOneHandler<?, ?> handler) {
        return resolveQueryType(handler.getClass(), "QueryOneHandler");
    }

    private static Class<?> resolveQueryType(Class<?> handlerClass, String className) {
        return Arrays.stream(handlerClass.getGenericInterfaces())
                .filter(t -> t.getTypeName().contains(className))
                .findFirst()
                .map(type -> {
                    try {
                        String cmdTypeName = type.getTypeName()
                                .replaceAll(".*<([^,>]+),.*", "$1");
                        return Class.forName(cmdTypeName);
                    } catch (Exception e) {
                        throw new IllegalStateException("Cannot resolve query type for " + handlerClass.getTypeName(), e);
                    }
                })
                .orElseThrow(() -> new IllegalStateException("Cannot infer query type"));
    }
}
