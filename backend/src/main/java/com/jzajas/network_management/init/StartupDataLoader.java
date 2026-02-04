package com.jzajas.network_management.init;

import com.jzajas.network_management.dtos.ConnectionJson;
import com.jzajas.network_management.dtos.DeviceJson;
import com.jzajas.network_management.dtos.NetworkTopologyJson;
import com.jzajas.network_management.entities.Connection;
import com.jzajas.network_management.entities.Device;
import com.jzajas.network_management.repositories.ConnectionRepository;
import com.jzajas.network_management.repositories.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class StartupDataLoader implements CommandLineRunner {
    private static final String TOPOLOGY_FILE_PATH = "data/topology.json";
    private final ObjectMapper objectMapper;
    private final DeviceRepository deviceRepository;
    private final ConnectionRepository connectionRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        InputStream inputStream =
                new ClassPathResource(TOPOLOGY_FILE_PATH).getInputStream();

        NetworkTopologyJson topology =
                objectMapper.readValue(inputStream, NetworkTopologyJson.class);

        Map<Long, Device> devicesById = new HashMap<>();

        for (DeviceJson dto : topology.getDevices()) {
            Device device = new Device(
                    dto.getId(),
                    dto.getName(),
                    dto.isActive()
            );

            devicesById.put(dto.getId(), device);
        }

        deviceRepository.saveAll(devicesById.values());

        List<Connection> connections = new ArrayList<>();

        for (ConnectionJson dto : topology.getConnections()) {
            Connection connection = new Connection();
            connection.setDeviceA(devicesById.get(dto.getFrom()));
            connection.setDeviceB(devicesById.get(dto.getTo()));
            connections.add(connection);
        }

        connectionRepository.saveAll(connections);
    }
}
