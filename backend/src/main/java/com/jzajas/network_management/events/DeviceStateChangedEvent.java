package com.jzajas.network_management.events;

public class DeviceStateChangedEvent {
    private Long deviceId;
    private boolean active;

    public DeviceStateChangedEvent(Long deviceId, boolean active) {
        this.deviceId = deviceId;
        this.active = active;
    }
}
