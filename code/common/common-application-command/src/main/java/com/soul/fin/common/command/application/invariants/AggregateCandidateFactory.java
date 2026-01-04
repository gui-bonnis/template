package com.soul.fin.common.command.application.invariants;

import com.soul.fin.common.core.event.DomainEvent;

import java.util.List;

public interface AggregateCandidateFactory {

    <T> AggregateCandidate<T> from(
            T aggregate,
            List<DomainEvent> newEvents
    );
}

