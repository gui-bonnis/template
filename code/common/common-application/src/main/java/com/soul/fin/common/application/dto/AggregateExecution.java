package com.soul.fin.common.application.dto;

import com.soul.fin.common.core.event.DomainEvent;

import java.util.List;

public record AggregateExecution<A>(
        A aggregate,
        List<DomainEvent> events
) {
}
