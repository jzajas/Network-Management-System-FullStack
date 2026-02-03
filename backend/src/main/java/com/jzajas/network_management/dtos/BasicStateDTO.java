package com.jzajas.network_management.dtos;

import com.jzajas.network_management.events.EventTypes;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class BasicStateDTO {
    private EventTypes type;
}
