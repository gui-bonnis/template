package com.soul.fin.common.command.application.policy;

import com.soul.fin.common.bus.core.command.Command;

public sealed interface PolicyAction {
    record EmitCommand(Command<?> c) implements PolicyAction {
    }

    record RaiseAlert(String message) implements PolicyAction {
    }

    record StartSaga(String sagaType) implements PolicyAction {
    }
}

