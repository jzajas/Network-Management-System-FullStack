package com.jzajas.network_management.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class NetworkTopologyDTO {
    List<DeviceDTO> devices;
    List<EdgeDTO> edges;
}
