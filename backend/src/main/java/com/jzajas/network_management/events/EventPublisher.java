package com.jzajas.network_management.events;

public interface EventPublisher {
    void publish(Object domainEvent);
}
