package com.jzajas.network_management.services;

import com.jzajas.network_management.dtos.BasicStateDTO;
import com.jzajas.network_management.dtos.DeviceStateChangeDTO;
import com.jzajas.network_management.dtos.InitialStateDTO;
import com.jzajas.network_management.events.DeltaDevices;
import com.jzajas.network_management.events.DeviceStateChangedEvent;
import com.jzajas.network_management.events.EventTypes;
import com.jzajas.network_management.sse.Subscription;
import com.jzajas.network_management.sse.SubscriptionRegistry;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SubscriptionServiceImplementation implements SubscriptionService {
    public static final String DEVICE_STATE_CHANGE_MESSAGE = "Device-State-Change";
    private final ReachabilityService reachabilityService;
    private final SubscriptionRegistry subscriptionRegistry;

    @Override
    public SseEmitter subscribe(Long rootDeviceId) {

        SseEmitter emitter = new SseEmitter(0L);

        Set<Long> initialReachable =
                reachabilityService.computeReachableFrom(rootDeviceId);

        Subscription subscription = new Subscription(
                rootDeviceId,
                emitter,
                initialReachable
        );

        UUID subscriptionId = subscriptionRegistry.addSubscription(subscription);

        emitter.onCompletion(() -> subscriptionRegistry.removeSubscription(subscriptionId));
        emitter.onTimeout(() -> subscriptionRegistry.removeSubscription(subscriptionId));
        emitter.onError(e -> subscriptionRegistry.removeSubscription(subscriptionId));

        send(emitter, new InitialStateDTO(
                EventTypes.INITIAL_STATE,
                List.copyOf(initialReachable)
        ));

        return emitter;
    }

    @EventListener
    public void onDeviceStateChanged(DeviceStateChangedEvent event) {

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

            for (Long removedId : delta.getRemoved()) {
                send(subscription.getEmitter(),
                        new DeviceStateChangeDTO(EventTypes.REMOVED, removedId));
            }

            for (Long addedId : delta.getAdded()) {
                send(subscription.getEmitter(),
                        new DeviceStateChangeDTO(EventTypes.ADDED, addedId));
            }

            subscription.updateLastReachable(newReachable);
        }
    }

    protected void send(SseEmitter emitter, BasicStateDTO dto) {
        try {
            emitter.send(
                    SseEmitter.event()
                            .name(DEVICE_STATE_CHANGE_MESSAGE)
                            .data(dto)
            );
        } catch (IOException e) {
            emitter.completeWithError(e);
        }
    }
}
