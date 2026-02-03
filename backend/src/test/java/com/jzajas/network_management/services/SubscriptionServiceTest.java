package com.jzajas.network_management.services;

import com.jzajas.network_management.dtos.InitialStateDTO;
import com.jzajas.network_management.events.EventTypes;
import com.jzajas.network_management.sse.SseEmitterFactory;
import com.jzajas.network_management.sse.Subscription;
import com.jzajas.network_management.sse.SubscriptionRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Set;
import java.util.UUID;

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
    private ReachabilityDeviceService reachabilityDeviceService;

    @Mock
    private SubscriptionRegistry subscriptionRegistry;

    @Mock
    private SseEmitterFactory sseEmitterFactory;

    @Spy
    @InjectMocks
    private SubscriptionServiceImplementation subscriptionService;

    private static final Long ROOT_DEVICE_ID = 1L;

    @Test
    void givenRootDeviceId_whenSubscribe_thenInitialStateIsSentAndSubscriptionRegistered() {
        Set<Long> reachable = Set.of(2L, 3L, 4L);
        UUID subscriptionId = UUID.randomUUID();
        SseEmitter emitter = new SseEmitter();

        when(reachabilityDeviceService.computeReachableFrom(ROOT_DEVICE_ID))
                .thenReturn(reachable);
        when(subscriptionRegistry.addSubscription(any()))
                .thenReturn(subscriptionId);
        when(sseEmitterFactory.create(any(), any()))
                .thenReturn(emitter);

        SseEmitter actualEmitter = subscriptionService.subscribe(ROOT_DEVICE_ID);

        assertNotNull(actualEmitter);

        verify(reachabilityDeviceService)
                .computeReachableFrom(ROOT_DEVICE_ID);
        verify(subscriptionRegistry)
                .addSubscription(any(Subscription.class));
    }

    @Test
    void givenSubscription_whenSubscribe_thenInitialStateIsSentOnlyOnce() {
        SseEmitter emitter = new SseEmitter();

        when(reachabilityDeviceService.computeReachableFrom(ROOT_DEVICE_ID))
                .thenReturn(Set.of(2L, 3L));
        when(subscriptionRegistry.addSubscription(any()))
                .thenReturn(UUID.randomUUID());
        when(sseEmitterFactory.create(any(), any()))
                .thenReturn(emitter);

        subscriptionService.subscribe(ROOT_DEVICE_ID);
    }
}
