package com.jzajas.network_management.services;

import com.jzajas.network_management.dtos.DeviceStateChangeDTO;
import com.jzajas.network_management.events.DeltaDevices;
import com.jzajas.network_management.events.DeviceStateChangedEvent;
import com.jzajas.network_management.events.EventTypes;
import com.jzajas.network_management.sse.SseEventPublisher;
import com.jzajas.network_management.sse.Subscription;
import com.jzajas.network_management.sse.SubscriptionRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReachabilitySubscriptionServiceTest {
    private static final Long DEFAULT_ID_1 = 1L;
    private static final Long DEFAULT_ID_2 = 2L;
    private static final Long DEFAULT_ID_3 = 3L;
    private static final Long WRONG_DEVICE_ID = 99L;
    @Mock
    private ReachabilityDeviceService reachabilityService;

    @Mock
    private SubscriptionRegistry subscriptionRegistry;

    @Mock
    private SseEventPublisher sseEventPublisher;

    @InjectMocks
    private ReachabilitySubscriptionServiceImplementation service;

    @Test
    void givenSubscriptionWithDelta_whenDeviceStateChanges_thenAddedAndRemovedEventsArePublished() {
        Set<Long> previousReachable = Set.of(DEFAULT_ID_1, DEFAULT_ID_2);
        Set<Long> newReachable = Set.of(DEFAULT_ID_2, DEFAULT_ID_3);

        Subscription subscription = new Subscription(
                DEFAULT_ID_1,
                null,
                previousReachable
        );

        when(subscriptionRegistry.getAllSubscriptions())
                .thenReturn(List.of(subscription));

        when(reachabilityService.computeReachableFrom(DEFAULT_ID_1))
                .thenReturn(newReachable);

        when(reachabilityService.computeDelta(previousReachable, newReachable))
                .thenReturn(new DeltaDevices(
                        Set.of(DEFAULT_ID_3),
                        Set.of(DEFAULT_ID_1)
                ));

        service.handleDeviceStateChange(new DeviceStateChangedEvent(WRONG_DEVICE_ID, false));

        verify(sseEventPublisher).publish(
                eq(subscription),
                argThat(dto ->
                        dto instanceof DeviceStateChangeDTO
                                && ((DeviceStateChangeDTO) dto).getType() == EventTypes.ADDED
                                && ((DeviceStateChangeDTO) dto).getDeviceId().equals(DEFAULT_ID_3)
                )
        );

        verify(sseEventPublisher).publish(
                eq(subscription),
                argThat(dto ->
                        dto instanceof DeviceStateChangeDTO
                                && ((DeviceStateChangeDTO) dto).getType() == EventTypes.REMOVED
                                && ((DeviceStateChangeDTO) dto).getDeviceId().equals(DEFAULT_ID_1)
                )
        );

        assertEquals(newReachable, subscription.getLastReachable());
    }
}
