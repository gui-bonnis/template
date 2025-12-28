package com.soul.fin.accounting;

import com.soul.fin.common.core.event.DomainEvent;
import com.soul.fin.common.core.event.Metadata;

import java.util.UUID;

public record EventEnvelope(
        UUID eventId,
        DomainEvent event,
        Metadata metadata
) {
}
