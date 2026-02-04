package com.jzajas.network_management.services;

import com.jzajas.network_management.events.DeviceStateChangedEvent;

public interface ReachabilitySubscriptionService {
    void handleDeviceStateChange(DeviceStateChangedEvent event);
}
