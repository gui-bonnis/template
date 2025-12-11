package com.soul.fin.common.core.entity;

import java.util.List;

public sealed interface DomainResult<T>
        permits DomainResult.Success, DomainResult.Failure {

    record Success<T>(T value) implements DomainResult<T> {
    }

    record Failure<T>(List<Violation> violations) implements DomainResult<T> {
    }
}
