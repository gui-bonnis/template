package com.soul.fin.common.command.application.invariants;

import com.soul.fin.common.core.event.DomainEvent;

import java.util.List;

public interface InvariantGuard {

    <T> ValidationResult validate(
            T aggregate,
            List<DomainEvent> newEvents
    );
}

