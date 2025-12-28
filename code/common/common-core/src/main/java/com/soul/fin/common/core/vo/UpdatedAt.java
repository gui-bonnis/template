package com.soul.fin.common.core.vo;

import java.time.Instant;

public sealed interface UpdatedAt permits UpdatedAt.At, UpdatedAt.Now {

    Instant value();

    record Now() implements UpdatedAt {
        private static final Instant value = Instant.now();

        @Override
        public Instant value() {
            return value;
        }
    }

    record At(Instant value) implements UpdatedAt {
        public At {
            if (value == null) {
                throw new IllegalArgumentException("value cannot be null");
            }
        }
    }
}
