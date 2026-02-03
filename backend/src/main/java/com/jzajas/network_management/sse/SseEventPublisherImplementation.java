package com.jzajas.network_management.sse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Component
public class SseEventPublisherImplementation implements SseEventPublisher {
    private static final String EVENT_NAME = "Device-State-Change";

    @Override
    public void publish(Subscription subscription, Object payload) {
        try {
            subscription.getEmitter().send(
                    SseEmitter.event()
                            .name(EVENT_NAME)
                            .data(payload)
            );
        } catch (IOException e) {
            subscription.getEmitter().completeWithError(e);
        }
    }
}