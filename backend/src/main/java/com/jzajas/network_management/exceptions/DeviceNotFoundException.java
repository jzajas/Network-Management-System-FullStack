package com.jzajas.network_management.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class DeviceNotFoundException extends RuntimeException{
    private Long deviceId;

    public DeviceNotFoundException(final Long deviceId) {
        this.deviceId = deviceId;
    }
}
