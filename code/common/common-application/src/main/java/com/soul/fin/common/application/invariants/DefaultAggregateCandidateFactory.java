package com.soul.fin.common.application.invariants;

import com.soul.fin.common.core.event.DomainEvent;
import com.soul.fin.common.core.vo.BaseId;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public final class DefaultAggregateCandidateFactory implements AggregateCandidateFactory {

    @Override
    public <T> AggregateCandidate<T> from(
            T aggregate,
            List<DomainEvent> newEvents
    ) {
        return new AggregateCandidate<>(
                extractId(aggregate),
                extractBaseVersion(aggregate),
                newEvents,
                aggregate
        );
    }

    private <T> BaseId<?> extractId(T aggregate) {
        return null;
    }

    private <T> long extractBaseVersion(T aggregate) {
        return 1L;
    }
}

