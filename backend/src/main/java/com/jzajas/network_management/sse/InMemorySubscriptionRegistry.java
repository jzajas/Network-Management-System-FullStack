package com.jzajas.network_management.sse;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class InMemorySubscriptionRegistry implements SubscriptionRegistry {

    private final ConcurrentMap<UUID, Subscription> subscriptions =
            new ConcurrentHashMap<>();

    @Override
    public UUID addSubscription(Subscription subscription) {
        UUID id = UUID.randomUUID();
        subscriptions.put(id, subscription);
        return id;
    }

    @Override
    public void removeSubscription(UUID subscriptionId) {
        subscriptions.remove(subscriptionId);
    }

    @Override
    public Optional<Subscription> getSubscription(UUID subscriptionId) {
        return Optional.ofNullable(subscriptions.get(subscriptionId));
    }

    @Override
    public Collection<Subscription> getAllSubscriptions() {
        return subscriptions.values();
    }
}
