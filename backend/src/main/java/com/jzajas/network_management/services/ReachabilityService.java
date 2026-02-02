package com.jzajas.network_management.services;

import java.util.Set;

public interface ReachabilityService {
    Set<Long> computeReachableFrom(Long sourceDeviceId);
}
