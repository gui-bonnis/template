package com.soul.fin.common.core.vo;

import java.time.Instant;

public sealed interface CreatedAt permits CreatedAt.At, CreatedAt.Now {

    Instant value();

    record Now() implements CreatedAt {
        private static final Instant value = Instant.now();

        @Override
        public Instant value() {
            return value;
        }
    }

    record At(Instant value) implements CreatedAt {
        public At {
            if (value == null) {
                throw new IllegalArgumentException("value cannot be null");
            }
        }
    }
}
