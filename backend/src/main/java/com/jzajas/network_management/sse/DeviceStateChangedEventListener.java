package com.jzajas.network_management.sse;

import com.jzajas.network_management.events.DeviceStateChangedEvent;
import com.jzajas.network_management.services.ReachabilitySubscriptionService;
import com.jzajas.network_management.services.SubscriptionService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DeviceStateChangedEventListener {

    ReachabilitySubscriptionService reachabilitySubscriptionService;

    @EventListener
    public void onDeviceStateChanged(DeviceStateChangedEvent event) {
        reachabilitySubscriptionService.handleDeviceStateChange(event);
    }
}
