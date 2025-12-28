package com.soul.fin.common.core.entity;

import java.time.Instant;

public sealed interface Audit {
    record createdAt(Instant createdAt) implements Audit {
    }

    record createdBy(Instant createdAt, String createdBy) implements Audit {
    }

    record updatedAt(Instant updatedAt) implements Audit {
    }

    record updatedBy(Instant updatedAt, String updatedBy) implements Audit {
    }

    record all(Instant createdAt, String createdBy, Instant updatedAt, String updatedBy) implements Audit {
    }
}
