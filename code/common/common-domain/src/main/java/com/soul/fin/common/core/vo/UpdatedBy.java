package com.soul.fin.common.core.vo;

public sealed interface UpdatedBy permits UpdatedBy.By, UpdatedBy.Anonymous {

    String userId();

    record Anonymous() implements UpdatedBy {
        private static final String value = "";

        @Override
        public String userId() {
            return value;
        }
    }

    record By(String userId) implements UpdatedBy {
        public By {
            if (userId == null) {
                throw new IllegalArgumentException("userId cannot be null");
            }
        }
    }
}
