package com.jzajas.network_management.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class DeltaDevices {
    private final Set<Long> added;
    private final Set<Long> removed;

    public boolean isEmpty() {
        return added.isEmpty() && removed.isEmpty();
    }
}
