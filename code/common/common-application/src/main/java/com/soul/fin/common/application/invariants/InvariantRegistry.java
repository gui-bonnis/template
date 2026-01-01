package com.soul.fin.common.application.invariants;

import java.util.List;

public interface InvariantRegistry {

    <T> List<InvariantValidator<T>> invariantsFor(Class<T> aggregateType);
}

