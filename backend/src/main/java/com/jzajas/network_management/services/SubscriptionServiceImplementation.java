package com.jzajas.network_management.services;

import com.jzajas.network_management.events.EventTypes;
import com.jzajas.network_management.events.DeltaDevices;
import com.jzajas.network_management.events.DeviceStateChangedEvent;
import com.jzajas.network_management.sse.Subscription;
import com.jzajas.network_management.sse.SubscriptionRegistry;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SubscriptionServiceImplementation implements SubscriptionService {
    private final ReachabilityService reachabilityService;
    private final ReachabilityService diffService;
    private final SubscriptionRegistry subscriptionRegistry;

    @Override
    public SseEmitter subscribe(Long rootDeviceId) {
        SseEmitter emitter = new SseEmitter(0L); // no timeout

        Set<Long> initialReachable =
                reachabilityService.computeReachableFrom(rootDeviceId);

        send(emitter, EventTypes.INITIAL_STATE.name(), initialReachable);

        Subscription subscription = new Subscription(
                rootDeviceId,
                emitter,
                initialReachable
        );

        UUID subscriptionId = subscriptionRegistry.addSubscription(subscription);

        emitter.onCompletion(() ->
                subscriptionRegistry.removeSubscription(subscriptionId)
        );
        emitter.onTimeout(() ->
                subscriptionRegistry.removeSubscription(subscriptionId)
        );
        emitter.onError(e ->
                subscriptionRegistry.removeSubscription(subscriptionId)
        );

        return emitter;
    }

    @EventListener
    public void on(DeviceStateChangedEvent event) {
        subscriptionRegistry.getAllSubscriptions()
                .forEach(subscription -> handleSubscription(subscription));
    }

    private void handleSubscription(Subscription subscription) {
        Long rootDeviceId = subscription.getRootDeviceId();

        Set<Long> newReachable =
                reachabilityService.computeReachableFrom(rootDeviceId);

        DeltaDevices delta = diffService.computeDelta(
                subscription.getLastReachable(),
                newReachable
        );

        if (!delta.isEmpty()) {
            send(subscription.getEmitter(), EventTypes.ADDED.name(), delta.getAdded());
            send(subscription.getEmitter(), EventTypes.REMOVED.name(), delta.getRemoved());

            subscription.updateLastReachable(newReachable);
        }
    }

    private void send(SseEmitter emitter, String eventName, Object data) {
        try {
            emitter.send(
                    SseEmitter.event()
                            .name(eventName)
                            .data(data)
            );
        } catch (IOException e) {
            emitter.completeWithError(e);
        }
    }
}
