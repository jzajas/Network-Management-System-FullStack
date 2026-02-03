package com.jzajas.network_management.sse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class Subscription {
    private final Long rootDeviceId;
    private final SseEmitter emitter;
    private Set<Long> lastReachable;

    public void updateLastReachable(Set<Long> lastReachable) {
        this.lastReachable = lastReachable;
    }
}
