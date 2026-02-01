package com.jzajas.network_management.events;


import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringDeviceEventPublisher implements EventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;


    @Override
    public void publish(Object domainEvent) {
        applicationEventPublisher.publishEvent(domainEvent);
    }
}
