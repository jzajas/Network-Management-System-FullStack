package com.jzajas.network_management.services;

import com.jzajas.network_management.dtos.InitialStateDTO;
import com.jzajas.network_management.events.EventTypes;
import com.jzajas.network_management.sse.SseEmitterFactory;
import com.jzajas.network_management.sse.SseEventPublisher;
import com.jzajas.network_management.sse.Subscription;
import com.jzajas.network_management.sse.SubscriptionRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceTest {
    private static final Long DEFAULT_ID_1 = 1L;
    private static final Long DEFAULT_ID_2 = 2L;
    private static final Long DEFAULT_ID_3 = 3L;
    private static final Long DEFAULT_TIMEOUT = 0L;
    private static final UUID SUBSCRIPTION_ID = UUID.randomUUID();

    @Mock
    private ReachabilityDeviceService reachabilityDeviceService;

    @Mock
    private SubscriptionRegistry subscriptionRegistry;

    @Mock
    private SseEmitterFactory sseEmitterFactory;

    @Mock
    private SseEventPublisher sseEventPublisher;

    @InjectMocks
    private SubscriptionServiceImplementation subscriptionService;

    @Test
    void givenRootDevice_whenSubscribe_thenSubscriptionIsRegisteredAndInitialStatePublished() {
        Set<Long> reachable = Set.of(DEFAULT_ID_2, DEFAULT_ID_3);
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);

        when(reachabilityDeviceService.computeReachableFrom(DEFAULT_ID_1))
                .thenReturn(reachable);

        when(subscriptionRegistry.addSubscription(any(Subscription.class)))
                .thenReturn(SUBSCRIPTION_ID);

        when(sseEmitterFactory.create(
                eq(SUBSCRIPTION_ID),
                any()
        )).thenReturn(emitter);

        SseEmitter result = subscriptionService.subscribe(DEFAULT_ID_1);

        assertSame(emitter, result);

        verify(reachabilityDeviceService)
                .computeReachableFrom(DEFAULT_ID_1);

        verify(subscriptionRegistry)
                .addSubscription(any(Subscription.class));

        verify(sseEmitterFactory)
                .create(eq(SUBSCRIPTION_ID), any());

        verify(sseEventPublisher)
                .publish(
                        any(Subscription.class),
                        argThat(dto ->
                                dto instanceof InitialStateDTO
                                        && ((InitialStateDTO) dto).getType() == EventTypes.INITIAL_STATE
                                        && ((InitialStateDTO) dto).getDeviceIds().containsAll(reachable)
                        )
                );
    }
}
