package com.soul.fin.common.application.policy.effects;

public final class NoOpPolicyEffects implements PolicyEffects {

    @Override
    public void alert(PolicyAlert alert) {
        // intentionally ignored
    }

    @Override
    public void emitCommand(Object command) {
        throw new UnsupportedOperationException(
                "Sync policies cannot emit commands"
        );
    }

    @Override
    public void startSaga(String sagaType, Object payload) {
        // intentionally ignored
    }
}

