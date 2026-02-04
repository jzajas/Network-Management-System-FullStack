package com.jzajas.network_management.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeviceStateChangedEvent {
    private Long deviceId;
    private boolean active;
}
