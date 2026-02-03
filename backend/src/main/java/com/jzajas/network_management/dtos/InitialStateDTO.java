package com.jzajas.network_management.dtos;

import com.jzajas.network_management.events.EventTypes;

import java.util.List;


public class InitialStateDTO {
    private EventTypes type;
    private List<Long> deviceIds;
}
