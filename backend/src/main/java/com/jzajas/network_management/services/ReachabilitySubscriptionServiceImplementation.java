package com.jzajas.network_management.services;

import com.jzajas.network_management.dtos.DeviceStateChangeDTO;
import com.jzajas.network_management.events.DeltaDevices;
import com.jzajas.network_management.events.DeviceStateChangedEvent;
import com.jzajas.network_management.events.EventTypes;
import com.jzajas.network_management.sse.SseEventPublisher;
import com.jzajas.network_management.sse.Subscription;
import com.jzajas.network_management.sse.SubscriptionRegistry;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
@AllArgsConstructor
public class ReachabilitySubscriptionServiceImplementation implements ReachabilitySubscriptionService{

    private final ReachabilityDeviceService reachabilityService;
    private final SubscriptionRegistry subscriptionRegistry;
    private final SseEventPublisher sseEventPublisher;

    @Override
    public void handleDeviceStateChange(DeviceStateChangedEvent event) {

        for (Subscription subscription : subscriptionRegistry.getAllSubscriptions()) {

            Set<Long> newReachable =
                    reachabilityService.computeReachableFrom(
                            subscription.getRootDeviceId()
                    );

            DeltaDevices delta =
                    reachabilityService.computeDelta(
                            subscription.getLastReachable(),
                            newReachable
                    );

            delta.getRemoved().forEach(id ->
                    sseEventPublisher.publish(
                            subscription,
                            new DeviceStateChangeDTO(EventTypes.REMOVED, id)
                    )
            );

            delta.getAdded().forEach(id ->
                    sseEventPublisher.publish(
                            subscription,
                            new DeviceStateChangeDTO(EventTypes.ADDED, id)
                    )
            );

            subscription.updateLastReachable(newReachable);
        }
    }
}
