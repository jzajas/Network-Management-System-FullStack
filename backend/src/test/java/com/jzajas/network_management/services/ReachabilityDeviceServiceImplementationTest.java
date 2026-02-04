package com.jzajas.network_management.services;

import com.jzajas.network_management.entities.Connection;
import com.jzajas.network_management.entities.Device;
import com.jzajas.network_management.events.DeltaDevices;
import com.jzajas.network_management.repositories.ConnectionRepository;
import com.jzajas.network_management.repositories.DeviceRepository;
import com.jzajas.network_management.util.GraphBuilder;
import com.jzajas.network_management.util.ReachabilityCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReachabilityDeviceServiceImplementationTest {
    private static final Long DEFAULT_ID_1 = 1L;
    private static final Long DEFAULT_ID_2 = 2L;
    private static final Long DEFAULT_ID_3 = 3L;
    private static final Long DEFAULT_ID_4 = 4L;

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private ConnectionRepository connectionRepository;

    @Mock
    private GraphBuilder graphBuilder;

    @Mock
    private ReachabilityCalculator reachabilityCalculator;

    @InjectMocks
    private ReachabilityDeviceServiceImplementation reachabilityService;

    @Test
    void givenDevicesAndConnectionsExist_whenComputeReachableFrom_thenGraphIsBuiltAndReachabilityCalculated() {
        Device device1 = new Device(DEFAULT_ID_1, "A", true);
        Device device2 = new Device(DEFAULT_ID_2, "B", false);
        List<Device> devices = List.of(device1, device2);
        Connection connection = mock(Connection.class);
        List<Connection> connections = List.of(connection);
        Map<Long, Set<Long>> graph = Map.of(
                DEFAULT_ID_1, Set.of(DEFAULT_ID_2),
                DEFAULT_ID_2, Set.of(DEFAULT_ID_1)
        );
        Map<Long, Boolean> activeMap = Map.of(
                DEFAULT_ID_1, true,
                DEFAULT_ID_2, false
        );
        Set<Long> reachable = Set.of(DEFAULT_ID_1);

        when(deviceRepository.findAll()).thenReturn(devices);
        when(connectionRepository.findAll()).thenReturn(connections);
        when(graphBuilder.buildGraph(devices, connections)).thenReturn(graph);
        when(reachabilityCalculator.calculateReachable(
                DEFAULT_ID_1, graph, activeMap
        )).thenReturn(reachable);

        Set<Long> result = reachabilityService.computeReachableFrom(DEFAULT_ID_1);

        assertEquals(reachable, result);

        verify(deviceRepository).findAll();
        verify(connectionRepository).findAll();
        verify(graphBuilder).buildGraph(devices, connections);
        verify(reachabilityCalculator).calculateReachable(
                DEFAULT_ID_1, graph, activeMap
        );
    }

    @Test
    void givenNoDevices_whenComputeReachableFrom_thenEmptyResultIsReturned() {
        when(deviceRepository.findAll()).thenReturn(List.of());
        when(connectionRepository.findAll()).thenReturn(List.of());
        when(graphBuilder.buildGraph(List.of(), List.of()))
                .thenReturn(Map.of());
        when(reachabilityCalculator.calculateReachable(
                eq(DEFAULT_ID_1),
                eq(Map.of()),
                eq(Map.of())
        )).thenReturn(Set.of());

        Set<Long> result = reachabilityService.computeReachableFrom(DEFAULT_ID_1);

        assertEquals(Set.of(), result);
    }

    @Test
    void givenPreviousAndCurrentReachable_whenComputeDelta_thenAddedAndRemovedAreCorrect() {
        Set<Long> previous = Set.of(DEFAULT_ID_1, DEFAULT_ID_2, DEFAULT_ID_3);
        Set<Long> current = Set.of(DEFAULT_ID_2, DEFAULT_ID_3, DEFAULT_ID_4);

        DeltaDevices delta = reachabilityService.computeDelta(previous, current);

        assertEquals(Set.of(DEFAULT_ID_4), delta.getAdded());
        assertEquals(Set.of(DEFAULT_ID_1), delta.getRemoved());
    }

    @Test
    void givenIdenticalReachableSets_whenComputeDelta_thenDeltaIsEmpty() {
        Set<Long> reachable = Set.of(DEFAULT_ID_1, DEFAULT_ID_2);

        DeltaDevices delta = reachabilityService.computeDelta(reachable, reachable);

        assertTrue(delta.isEmpty());
    }

    @Test
    void givenEmptyPrevious_whenComputeDelta_thenAllCurrentAreAdded() {
        Set<Long> previous = Set.of();
        Set<Long> current = Set.of(DEFAULT_ID_1, DEFAULT_ID_2);

        DeltaDevices delta = reachabilityService.computeDelta(previous, current);

        assertEquals(Set.of(DEFAULT_ID_1, DEFAULT_ID_2), delta.getAdded());
        assertEquals(Set.of(), delta.getRemoved());
    }
}
