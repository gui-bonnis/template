package com.soul.fin.common.core.util;

import com.soul.fin.common.core.entity.Violation;

import java.util.List;

public interface Validator {
    <T> List<Violation> validate(T target);
}
