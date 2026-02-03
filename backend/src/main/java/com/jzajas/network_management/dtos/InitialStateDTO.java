package com.jzajas.network_management.dtos;

import com.jzajas.network_management.events.EventTypes;
import lombok.Getter;

import java.util.List;

@Getter
public class InitialStateDTO extends BasicStateDTO {
    private List<Long> deviceIds;

    public InitialStateDTO(EventTypes type, List<Long> deviceIds) {
        super(type);
        this.deviceIds = deviceIds;
    }
}
