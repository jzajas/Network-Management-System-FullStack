package com.jzajas.network_management.dtos;

import lombok.Getter;

import java.util.List;

@Getter
public class NetworkTopologyJson {
    private List<DeviceJson> devices;
    private List<ConnectionJson> connections;
}
