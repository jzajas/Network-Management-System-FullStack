package com.jzajas.network_management.configuration;

import com.jzajas.network_management.sse.InMemorySubscriptionRegistry;
import com.jzajas.network_management.sse.SubscriptionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SubscriptionConfiguration {

    @Bean
    public SubscriptionRegistry subscriptionRegistry() {
        return new InMemorySubscriptionRegistry();
    }
}
