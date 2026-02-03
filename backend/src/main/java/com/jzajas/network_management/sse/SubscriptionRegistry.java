package com.jzajas.network_management.sse;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRegistry {
    UUID addSubscription(Subscription subscription);
    void removeSubscription(UUID subscriptionId);
    Optional<Subscription> getSubscription(UUID subscriptionId);
    Collection<Subscription> getAllSubscriptions();
}
