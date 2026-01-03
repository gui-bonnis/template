package com.soul.fin.common.bus.core.query;

public sealed interface Query<R>
        permits QueryOne, QueryMany {
}