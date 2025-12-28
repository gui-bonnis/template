package com.soul.fin.common.core.vo;

public sealed interface CreatedBy permits CreatedBy.By, CreatedBy.Anonymous {

    String userId();

    record Anonymous() implements CreatedBy {
        private static final String value = "";

        @Override
        public String userId() {
            return value;
        }
    }

    record By(String userId) implements CreatedBy {
        public By {
            if (userId == null) {
                throw new IllegalArgumentException("userId cannot be null");
            }
        }
    }
}
