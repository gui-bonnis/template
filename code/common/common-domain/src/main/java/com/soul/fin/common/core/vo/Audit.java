package com.soul.fin.common.core.vo;


public sealed interface Audit permits Audit.New, Audit.Update {
    CreatedAt createdAt();

    CreatedBy createdBy();

    UpdatedAt updatedAt();

    UpdatedBy updatedBy();

    record New(CreatedBy createdBy) implements Audit {
        private static CreatedAt createdAt;
        private static UpdatedAt updatedAt;
        private static UpdatedBy updatedBy;

        public New {
            if (createdBy instanceof CreatedBy.By(String userId)) {
                if (userId == null) {
                    throw new IllegalArgumentException("createdBy cannot be null");
                }
                updatedBy = new UpdatedBy.By(userId);
            } else {
                updatedBy = new UpdatedBy.Anonymous();
            }
            createdAt = new CreatedAt.Now();
            updatedAt = new UpdatedAt.Now();
        }

        @Override
        public CreatedAt createdAt() {
            return createdAt;
        }

        @Override
        public CreatedBy createdBy() {
            return createdBy;
        }

        @Override
        public UpdatedAt updatedAt() {
            return updatedAt;
        }

        @Override
        public UpdatedBy updatedBy() {
            return updatedBy;
        }
    }

    record Update(Audit audit,
                  UpdatedBy updatedBy) implements Audit {
        private static UpdatedAt updatedAt;

        public Update {
            updatedAt = new UpdatedAt.Now();
            if (updatedBy instanceof UpdatedBy.By(String userId)) {
                if (userId == null) {
                    throw new IllegalArgumentException("updatedBy cannot be null");
                }
            }
        }

        @Override
        public CreatedAt createdAt() {
            return audit.createdAt();
        }

        @Override
        public CreatedBy createdBy() {
            return audit.createdBy();
        }

        @Override
        public UpdatedAt updatedAt() {
            return updatedAt;
        }

        @Override
        public UpdatedBy updatedBy() {
            return updatedBy;
        }
    }

}