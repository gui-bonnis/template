package com.soul.fin;

import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandBusConfig {

//    @Bean
//    @SuppressWarnings("unchecked")
//    public <C extends Command<?>> HandlerRegistry handlerRegistry(List<CommandHandler<?>> handlers) {
//        HandlerRegistry registry = new HandlerRegistry();
//
//        for (CommandHandler<?> handler : handlers) {
//            Class<?> cmdType = resolveCommandType(handler);
//            registry.register((Class<C>) cmdType, (CommandHandler<C>) handler);
//        }
//
//        return registry;
//    }
//
//    private static <C extends Command<?>> Class<?> resolveCommandType(CommandHandler<C> handler) {
//        return Arrays.stream(handler.getClass().getGenericInterfaces())
//                .filter(t -> t.getTypeName().contains("CommandHandler"))
//                .findFirst()
//                .map(type -> {
//                    try {
//                        String cmdTypeName = type.getTypeName()
//                                .replaceAll(".*<([^,>]+),.*", "$1");
//                        return Class.forName(cmdTypeName);
//                    } catch (Exception e) {
//                        throw new IllegalStateException("Cannot resolve command type for " + handler, e);
//                    }
//                })
//                .orElseThrow(() -> new IllegalStateException("Cannot infer command type"));
//    }
//
//    @Bean
//    public AsyncReactiveCommandBus commandBus(HandlerRegistry handlerRegistry, List<CommandMiddleware> middlewares) {
//        return new AsyncReactiveCommandBus(handlerRegistry, middlewares);
//    }
}
