package com.soul.fin.common.command.application.invariants;

import java.util.List;

public sealed interface ValidationResult
        permits ValidationResult.Valid, ValidationResult.Invalid {

    boolean isValid();

    record Valid() implements ValidationResult {
        @Override
        public boolean isValid() {
            return true;
        }
    }

    record Invalid(List<Violation> violations)
            implements ValidationResult {
        @Override
        public boolean isValid() {
            return false;
        }
    }
}

