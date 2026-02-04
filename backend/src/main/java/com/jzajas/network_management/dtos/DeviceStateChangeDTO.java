package com.jzajas.network_management.dtos;

import com.jzajas.network_management.events.EventTypes;
import lombok.Getter;

@Getter
public class DeviceStateChangeDTO extends BasicStateDTO{
    private Long deviceId;

    public DeviceStateChangeDTO(EventTypes type, Long deviceID) {
        super(type);
        deviceId = deviceID;
    }
}
