package com.soul.fin.common.projection.dsl;


public abstract class ProjectionDsl extends BaseProjection {

    protected <T> void on(
            String eventType,
            long schemaVersion,
            Class<T> payloadType,
            TypedEventHandler<T> handler
    ) {
        super.on(eventType, schemaVersion, event -> {
            T payload = payload(event, payloadType);
            return handler.handle(payload, event);
        });
    }
}

