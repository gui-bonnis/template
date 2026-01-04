package com.soul.fin.common.projection.dsl;

import com.soul.fin.common.projection.api.Projection;
import com.soul.fin.common.projection.error.exception.ProjectionDeserializationException;
import com.soul.fin.common.projection.error.exception.UnsupportedProjectionEventException;
import com.soul.fin.common.projection.model.EventEnvelope;
import reactor.core.publisher.Mono;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class BaseProjection implements Projection {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final Map<String, Map<Long, Function<EventEnvelope, Mono<Void>>>> handlers =
            new HashMap<>();

    protected BaseProjection() {
        registerHandlers();
    }

    /**
     * Projection unique name (used for offsets)
     */
    @Override
    public abstract String name();

    /**
     * Register all event handlers here
     */
    protected abstract void registerHandlers();

    /**
     * DSL entrypoint
     */
    protected void on(
            String eventType,
            long schemaVersion,
            Function<EventEnvelope, Mono<Void>> handler
    ) {
        handlers
                .computeIfAbsent(eventType, __ -> new HashMap<>())
                .put(schemaVersion, handler);
    }

    @Override
    public boolean supports(EventEnvelope event) {
        if (!handlers.containsKey(event.eventType())) {
            return false; // intentionally ignored
        }
        if (!handlers.get(event.eventType())
                .containsKey(event.eventSchemaVersion())) {
            throw new UnsupportedProjectionEventException(name(), event);
        }
        return true;
    }

    @Override
    public Mono<Void> project(EventEnvelope event) {
        var byVersion = handlers.get(event.eventType());

        if (byVersion == null) {
            return Mono.empty();
        }

        var handler = byVersion.get(event.eventSchemaVersion());

        if (handler == null) {
            return Mono.error(
                    new IllegalStateException(
                            "Unsupported schema version " + event.eventSchemaVersion() +
                                    " for event " + event.eventType()
                    )
            );
        }

        return handler.apply(event);
    }

    protected <T> T payload(EventEnvelope event, Class<T> type) {
        try {
            return OBJECT_MAPPER.readValue(event.payload(), type);
        } catch (Exception e) {
            throw new ProjectionDeserializationException(
                    name(),
                    event,
                    e
            );
        }
    }

    protected <T> T metadata(EventEnvelope event, Class<T> type) {
        try {
            return OBJECT_MAPPER.readValue(event.metadata(), type);
        } catch (Exception e) {
            throw new ProjectionDeserializationException(name(), event, e);
        }
    }

}

