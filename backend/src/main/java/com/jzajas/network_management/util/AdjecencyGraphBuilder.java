package com.jzajas.network_management.util;

import com.jzajas.network_management.entities.Connection;
import com.jzajas.network_management.entities.Device;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AdjecencyGraphBuilder implements  GraphBuilder{

    @Override
    public Map<Long, Set<Long>> buildGraph(List<Device> devices, List<Connection> connections) {
        Map<Long, Set<Long>> graph = new HashMap<>();

        for (Device device : devices) {
            graph.put(device.getId(), new HashSet<>());
        }

        for (Connection connection : connections) {
            Long a = connection.getDeviceA().getId();
            Long b = connection.getDeviceB().getId();

            graph.get(a).add(b);
            graph.get(b).add(a);
        }

        return graph;
    }
}
