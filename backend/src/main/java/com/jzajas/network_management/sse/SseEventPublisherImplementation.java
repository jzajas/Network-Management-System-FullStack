package com.jzajas.network_management.sse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Component
public class SseEventPublisherImplementation implements SseEventPublisher {

    @Override
    public void publish(Subscription subscription, Object payload, String eventName) {
        try {
            subscription.getEmitter().send(
                    SseEmitter.event()
                            .name(eventName)
                            .data(payload)
            );
        } catch (IOException e) {
            subscription.getEmitter().completeWithError(e);
        }
    }
}