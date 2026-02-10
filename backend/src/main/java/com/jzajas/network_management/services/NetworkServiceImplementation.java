package com.jzajas.network_management.services;

import com.jzajas.network_management.dtos.DeviceDTO;
import com.jzajas.network_management.dtos.EdgeDTO;
import com.jzajas.network_management.dtos.NetworkTopologyDTO;
import com.jzajas.network_management.entities.Connection;
import com.jzajas.network_management.entities.Device;
import com.jzajas.network_management.repositories.ConnectionRepository;
import com.jzajas.network_management.repositories.DeviceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class NetworkServiceImplementation implements NetworkService{

    private final DeviceRepository deviceRepository;
    private final ConnectionRepository connectionRepository;

    @Override
    @Transactional(readOnly = true)
    public NetworkTopologyDTO getNetworkTopology() {
        List<Device> devices = deviceRepository.findAll();
        List<Connection> connections = connectionRepository.findAll();

        List<DeviceDTO> deviceDTOs = devices.stream()
                .map(this::toDeviceDTO)
                .toList();

        List<EdgeDTO> edgeDTOs = connections.stream()
                .map(this::toEdgeDTO)
                .toList();

        return new NetworkTopologyDTO(deviceDTOs, edgeDTOs);
    }

    private DeviceDTO toDeviceDTO(Device device) {
        return new DeviceDTO(
                device.getId(),
                device.getName()
        );
    }
    private EdgeDTO toEdgeDTO(Connection connection) {
        return new EdgeDTO(
               connection.getDeviceA().getId(),
                connection.getDeviceB().getId()
        );
    }
}
