package com.soul.fin.common.bus;

import com.soul.fin.common.bus.core.Command;
import com.soul.fin.common.bus.core.CommandBus;
import com.soul.fin.common.bus.core.CommandHandler;
import com.soul.fin.common.bus.middleware.CommandMiddleware;
import com.soul.fin.common.bus.middleware.NextCommand;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SpringCommandBus implements CommandBus {

    private final Map<Class<?>, CommandHandler<?, ?>> handlers = new ConcurrentHashMap<>();
    private final List<CommandMiddleware> middlewares;

    public SpringCommandBus(List<CommandHandler<?, ?>> handlerBeans,
                            List<CommandMiddleware> middlewareBeans) {
        handlerBeans.forEach(handler -> {
            Class<?> cmdType = resolveCommandType(handler);
            handlers.put(cmdType, handler);
        });
        // respect Spring's @Order annotation
        middlewareBeans.sort(Comparator.comparingInt(SpringCommandBus::getOrder));
        this.middlewares = List.copyOf(middlewareBeans);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R, C extends Command<R>> Mono<R> execute(C command) {
        NextCommand chain = new NextCommand() {
            @Override
            public <R, C extends Command<R>> Mono<R> invoke(C cmd) {
                return Mono.just(cmd)
                        .flatMap(c -> {
                            @SuppressWarnings("unchecked")
                            var handler = (CommandHandler<C, R>) handlers.get(c.getClass());
                            if (handler == null) {
                                return Mono.error(new IllegalArgumentException("No handler for " + c.getClass()));
                            }
                            return handler.handle(c);
                        });
            }
        };

        // Build middleware chain (reverse order)
        ListIterator<CommandMiddleware> iterator = middlewares.listIterator(middlewares.size());
        while (iterator.hasPrevious()) {
            CommandMiddleware mw = iterator.previous();
            NextCommand nextCopy = chain;
            chain = new NextCommand() {
                @Override
                public <R, C extends Command<R>> Mono<R> invoke(C cmd) {
                    return mw.invoke(cmd, nextCopy);
                }
            };
        }

        return chain.invoke(command);
    }

    private static int getOrder(CommandMiddleware mw) {
        Order order = mw.getClass().getAnnotation(Order.class);
        return order != null ? order.value() : 0;
    }

    private static Class<?> resolveCommandType(CommandHandler<?, ?> handler) {
        return Arrays.stream(handler.getClass().getGenericInterfaces())
                .filter(t -> t.getTypeName().contains("CommandHandler"))
                .findFirst()
                .map(type -> {
                    try {
                        String cmdTypeName = type.getTypeName()
                                .replaceAll(".*<([^,>]+),.*", "$1");
                        return Class.forName(cmdTypeName);
                    } catch (Exception e) {
                        throw new IllegalStateException("Cannot resolve command type for " + handler, e);
                    }
                })
                .orElseThrow(() -> new IllegalStateException("Cannot infer command type"));
    }
}
