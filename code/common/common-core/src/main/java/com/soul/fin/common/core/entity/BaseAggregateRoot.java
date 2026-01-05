package com.soul.fin.common.core.entity;


import com.soul.fin.common.core.event.DomainEvent;
import com.soul.fin.common.core.vo.BaseId;

import java.util.ArrayList;
import java.util.List;

public class BaseAggregateRoot<ID extends BaseId<?>> extends AggregateRoot<ID> {
    protected List<String> failureMessages;
    protected List<DomainEvent> events;

    public static final String FAILURE_MESSAGE_DELIMITER = ",";

    protected void updateFailureMessages(List<String> failureMessages) {
        if (this.failureMessages != null && failureMessages != null) {
            this.failureMessages.addAll(failureMessages.stream().filter(message -> !message.isEmpty()).toList());
        }
        if (this.failureMessages == null) {
            this.failureMessages = failureMessages;
        }
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }

    public void registerEvent(DomainEvent event) {
        if (this.events == null) {
            this.events = new ArrayList<>();
        }
        this.events.add(event);
    }

    public List<DomainEvent> pullEvents() {
        if (this.events == null) {
            return List.of();
        }
        var copy = List.copyOf(this.events);
        events.clear();
        return copy;
    }


}
