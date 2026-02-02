package com.jzajas.network_management.services;

import com.jzajas.network_management.events.DeltaDevices;

import java.util.Set;

public interface ReachabilityService {
    Set<Long> computeReachableFrom(Long sourceDeviceId);
    DeltaDevices computeDelta(
            Set<Long> previousReachable,
            Set<Long> currentReachable
    );
}
