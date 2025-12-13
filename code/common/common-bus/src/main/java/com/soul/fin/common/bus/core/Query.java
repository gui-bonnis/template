package com.soul.fin.common.bus.core;

public sealed interface Query<R>
        permits QueryOne, QueryMany {
}