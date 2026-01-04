package com.soul.fin.common.command.application.policy;

public interface AsyncPolicy<E> {

    void onEvent(
            E event,
            PolicyContext<E> context
    );
}

