package com.jzajas.network_management.util;

import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

@Component
public class BfsReachabilityCalculator implements ReachabilityCalculator {
    @Override
    public Set<Long> calculateReachable(
            Long startDeviceId,
            Map<Long, Set<Long>> graph,
            Map<Long, Boolean> deviceActiveMap
    ) {
        Set<Long> reachable = new HashSet<>();

        if (!Boolean.TRUE.equals(deviceActiveMap.get(startDeviceId))) {
            return reachable;
        }

        Queue<Long> queue = new ArrayDeque<>();
        queue.add(startDeviceId);

        while (!queue.isEmpty()) {
            Long current = queue.poll();

            for (Long neighbor : graph.getOrDefault(current, Set.of())) {
                if (reachable.contains(neighbor)) continue;
                if (!Boolean.TRUE.equals(deviceActiveMap.get(neighbor))) continue;

                reachable.add(neighbor);
                queue.add(neighbor);
            }
        }

        reachable.remove(startDeviceId);
        return reachable;
    }
}
