package com.jzajas.network_management.dtos;

import com.jzajas.network_management.events.EventTypes;
import lombok.AllArgsConstructor;

public class DeviceStateChangeDTO extends BasicStateDTO{
    private Long deviceID;

    public DeviceStateChangeDTO(final EventTypes type, final Long deviceID) {
        super(type);
        this.deviceID = deviceID;
    }
}
