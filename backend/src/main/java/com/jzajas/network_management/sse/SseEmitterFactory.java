package com.jzajas.network_management.sse;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;
import java.util.function.Consumer;

public interface SseEmitterFactory {
    SseEmitter create(
            UUID subscriptionId,
            Consumer<UUID> onCompletion
    );
}
