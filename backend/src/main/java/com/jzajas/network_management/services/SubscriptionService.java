package com.jzajas.network_management.services;

import com.jzajas.network_management.events.DeviceStateChangedEvent;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SubscriptionService {
    SseEmitter subscribe(Long rootDeviceId);
}
