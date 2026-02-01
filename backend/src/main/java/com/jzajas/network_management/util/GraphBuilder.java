package com.jzajas.network_management.util;

import com.jzajas.network_management.entities.Connection;
import com.jzajas.network_management.entities.Device;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface GraphBuilder {
    Map<Long, Set<Long>> buildGraph(
            List<Device> devices,
            List<Connection> connection
    );
}
