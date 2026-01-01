package com.soul.fin.common.application.policy;

public record PolicyMetadata(
        String name,
        String version,
        Severity severity
) {
}


