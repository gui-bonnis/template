package com.soul.fin.common.application.dto;

import java.util.List;

public record ExecutionContext<A>(
        A aggregate,
        List<EventEnvelope> envelopes
) {
}
