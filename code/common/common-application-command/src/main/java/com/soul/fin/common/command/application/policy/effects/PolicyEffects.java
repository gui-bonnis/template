package com.soul.fin.common.command.application.policy.effects;

public interface PolicyEffects {

    void alert(PolicyAlert alert);

    void emitCommand(Object command);

    void startSaga(String sagaType, Object payload);
}

