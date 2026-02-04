package com.jzajas.network_management.sse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;
import java.util.function.Consumer;

@Component
public class SseEmitterFactoryImplementation implements SseEmitterFactory {
    private static final long TIMEOUT = 0L;

    @Override
    public SseEmitter create(UUID subscriptionId, Consumer<UUID> onCompletion) {
        SseEmitter emitter = new SseEmitter(TIMEOUT);

        emitter.onCompletion(() -> onCompletion.accept(subscriptionId));
        emitter.onTimeout(() -> onCompletion.accept(subscriptionId));
        emitter.onError(e -> onCompletion.accept(subscriptionId));

        return emitter;
    }
}
