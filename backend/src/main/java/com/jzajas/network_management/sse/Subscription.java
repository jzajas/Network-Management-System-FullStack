package com.jzajas.network_management.sse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class Subscription {
    private final Long rootDeviceId;
    private SseEmitter emitter;
    private Set<Long> lastReachable;

    public void updateLastReachable(Set<Long> lastReachable) {
        this.lastReachable = lastReachable;
    }
}
