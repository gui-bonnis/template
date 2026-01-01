package com.soul.fin.common.application.invariants;

import com.soul.fin.common.core.event.DomainEvent;
import com.soul.fin.common.core.vo.BaseId;

import java.util.List;

public record AggregateCandidate<T>(
        BaseId<?> id,
        long baseVersion,
        List<DomainEvent> newEvents,
        T projectedState
) {
}