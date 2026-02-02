package com.jzajas.network_management.util;

import java.util.Map;
import java.util.Set;

public interface ReachabilityCalculator {
    Set<Long> calculateReachable(
            Long startDeviceId,
            Map<Long, Set<Long>> graph,
            Map<Long, Boolean> deviceActiveMap
    );
}
