package com.soul.fin.common.core.publisher;

import com.soul.fin.common.core.event.DomainEvent;

import java.util.concurrent.Flow;

public interface EventPublisher<T extends DomainEvent> {

    //void publish(T domainEvent);

    Flow.Publisher<T> publish(T domainEvent);


}