package com.soul.fin.common.application.dto;

import tools.jackson.databind.JsonNode;

import java.util.UUID;

public record SnapshotEnvelope(
        UUID aggregateId,
        String aggregateType,
        long aggregateVersion,
        long globalPosition,
        long snapshotSchemaVersion,
        JsonNode payload
) {
}

