package com.jzajas.network_management.sse;

public interface SseEventPublisher {
    void publish(Subscription subscription, Object payload, String eventName);
}
