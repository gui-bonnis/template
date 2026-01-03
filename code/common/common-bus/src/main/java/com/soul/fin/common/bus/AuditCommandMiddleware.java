package com.soul.fin.common.bus;

import com.soul.fin.common.bus.core.command.Command;
import com.soul.fin.common.bus.middleware.CommandMiddleware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Function;

@Order(10)
@Component
public final class AuditCommandMiddleware implements CommandMiddleware {
    @Override
    public <R, C extends Command<R>> Function<C, Mono<R>> apply(Function<C, Mono<R>> next) {
        return cmd -> {
            cmd.metadata().setCommandId(UUID.randomUUID());
            cmd.metadata().setCausationId(UUID.randomUUID()); //previous eventId or commandId
            cmd.metadata().setCorrelationId(UUID.randomUUID()); // propagated from inbound request (MDC)
            cmd.metadata().setActor("user | system"); // from security / auth context
            cmd.metadata().setTenantId(""); // request / context

            return next.apply(cmd);
        };
    }
}
