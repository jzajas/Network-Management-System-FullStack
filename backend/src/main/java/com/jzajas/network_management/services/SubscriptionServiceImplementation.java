package com.jzajas.network_management.services;

import com.jzajas.network_management.dtos.InitialStateDTO;
import com.jzajas.network_management.events.EventTypes;
import com.jzajas.network_management.sse.SseEmitterFactory;
import com.jzajas.network_management.sse.SseEventPublisher;
import com.jzajas.network_management.sse.Subscription;
import com.jzajas.network_management.sse.SubscriptionRegistry;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SubscriptionServiceImplementation implements SubscriptionService {
    private static final String INITIAL_STATE_EVENT_NAME = "Initial State";

    private final ReachabilityDeviceService reachabilityDeviceService;
    private final SubscriptionRegistry subscriptionRegistry;
    private final SseEmitterFactory sseEmitterFactory;
    private final SseEventPublisher sseEventPublisher;


    @Override
    public SseEmitter subscribe(Long rootDeviceId) {

        Set<Long> initialReachable =
                reachabilityDeviceService.computeReachableFrom(rootDeviceId);

        Subscription subscription = new Subscription(
                rootDeviceId,
                null,
                initialReachable
        );

        UUID subscriptionId = subscriptionRegistry.addSubscription(subscription);

        SseEmitter emitter =
                sseEmitterFactory.create(
                        subscriptionId,
                        subscriptionRegistry::removeSubscription
                );

        subscription.setEmitter(emitter);

        sseEventPublisher.publish(
                subscription,
                new InitialStateDTO(
                        EventTypes.INITIAL_STATE,
                        List.copyOf(initialReachable)
                ),
                INITIAL_STATE_EVENT_NAME
        );

        return emitter;
    }
}
