package com.soul.fin.common.application.policy.effects;

public interface PolicyEffects {

    void alert(PolicyAlert alert);

    void emitCommand(Object command);

    void startSaga(String sagaType, Object payload);
}

