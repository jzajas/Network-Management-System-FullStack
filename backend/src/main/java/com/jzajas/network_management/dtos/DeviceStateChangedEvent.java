package com.jzajas.network_management.dtos;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEvent;


public class DeviceStateChangedEvent extends ApplicationEvent {
    private Long deviceId;
    private boolean active;

    public DeviceStateChangedEvent(Object source, Long deviceId, boolean active) {
        super(source);
        this.deviceId = deviceId;
        this.active = active;
    }
}
