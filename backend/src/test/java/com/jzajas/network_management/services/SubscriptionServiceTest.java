package com.jzajas.network_management.services;

import com.jzajas.network_management.dtos.InitialStateDTO;
import com.jzajas.network_management.events.EventTypes;
import com.jzajas.network_management.sse.Subscription;
import com.jzajas.network_management.sse.SubscriptionRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceTest {

    @Mock
    private ReachabilityService reachabilityService;

    @Mock
    private SubscriptionRegistry subscriptionRegistry;

    @Spy
    @InjectMocks
    private SubscriptionServiceImplementation subscriptionService;

    private static final Long ROOT_DEVICE_ID = 1L;

    @Test
    void givenRootDeviceId_whenSubscribe_thenInitialStateIsSentAndSubscriptionRegistered() {
        Set<Long> reachable = Set.of(2L, 3L, 4L);
        UUID subscriptionId = UUID.randomUUID();

        when(reachabilityService.computeReachableFrom(ROOT_DEVICE_ID))
                .thenReturn(reachable);
        when(subscriptionRegistry.addSubscription(any()))
                .thenReturn(subscriptionId);

        SseEmitter emitter = subscriptionService.subscribe(ROOT_DEVICE_ID);

        assertNotNull(emitter);

        verify(reachabilityService)
                .computeReachableFrom(ROOT_DEVICE_ID);
        verify(subscriptionRegistry)
                .addSubscription(any(Subscription.class));
        verify(subscriptionService).send(
                same(emitter),
                argThat(dto ->
                        dto instanceof InitialStateDTO &&
                                ((InitialStateDTO) dto).getType() == EventTypes.INITIAL_STATE &&
                                ((InitialStateDTO) dto).getDeviceIds().containsAll(reachable)
                )
        );
    }

    @Test
    void givenSubscription_whenSubscribe_thenInitialStateIsSentOnlyOnce() {
        when(reachabilityService.computeReachableFrom(ROOT_DEVICE_ID))
                .thenReturn(Set.of(2L, 3L));

        when(subscriptionRegistry.addSubscription(any()))
                .thenReturn(UUID.randomUUID());

        subscriptionService.subscribe(ROOT_DEVICE_ID);

        verify(subscriptionService, times(1))
                .send(any(SseEmitter.class), any(InitialStateDTO.class));
    }
}
