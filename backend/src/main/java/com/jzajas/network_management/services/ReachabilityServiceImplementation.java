package com.jzajas.network_management.services;

import com.jzajas.network_management.entities.Connection;
import com.jzajas.network_management.entities.Device;
import com.jzajas.network_management.repositories.ConnectionRepository;
import com.jzajas.network_management.repositories.DeviceRepository;
import com.jzajas.network_management.util.GraphBuilder;
import com.jzajas.network_management.util.ReachabilityCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReachabilityServiceImplementation implements ReachabilityService{

    private final DeviceRepository deviceRepository;
    private final ConnectionRepository connectionRepository;
    private final GraphBuilder graphBuilder;
    private final ReachabilityCalculator reachabilityCalculator;

    @Transactional(readOnly = true)
    @Override
    public Set<Long> computeReachableFrom(Long sourceDeviceId) {
        List<Device> devices = deviceRepository.findAll();
        List<Connection> connections = connectionRepository.findAll();

        Map<Long, Set<Long>> graph =
                graphBuilder.buildGraph(devices, connections);

        Map<Long, Boolean> deviceActiveMap = devices.stream()
                .collect(Collectors.toMap(
                        Device::getId,
                        Device::isActive
                ));

        return reachabilityCalculator.calculateReachable(
                sourceDeviceId,
                graph,
                deviceActiveMap
        );
    }
}
