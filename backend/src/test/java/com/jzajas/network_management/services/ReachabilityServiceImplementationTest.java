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
public class ReachabilityServiceImplementationTest {

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private ConnectionRepository connectionRepository;

    @Mock
    private GraphBuilder graphBuilder;

    @Mock
    private ReachabilityCalculator reachabilityCalculator;

    @InjectMocks
    private ReachabilityServiceImplementation reachabilityService;

    @Test
    void givenDevicesAndConnectionsExist_whenComputeReachableFrom_thenGraphIsBuiltAndReachabilityCalculated() {
        Long sourceDeviceId = 1L;
        Device device1 = new Device(1L, "A", true);
        Device device2 = new Device(2L, "B", false);
        List<Device> devices = List.of(device1, device2);

        Connection connection = mock(Connection.class);
        List<Connection> connections = List.of(connection);

        Map<Long, Set<Long>> graph = Map.of(
                1L, Set.of(2L),
                2L, Set.of(1L)
        );

        Map<Long, Boolean> activeMap = Map.of(
                1L, true,
                2L, false
        );

        Set<Long> reachable = Set.of(1L);

        when(deviceRepository.findAll()).thenReturn(devices);
        when(connectionRepository.findAll()).thenReturn(connections);
        when(graphBuilder.buildGraph(devices, connections)).thenReturn(graph);
        when(reachabilityCalculator.calculateReachable(
                sourceDeviceId, graph, activeMap
        )).thenReturn(reachable);

        Set<Long> result = reachabilityService.computeReachableFrom(sourceDeviceId);

        assertEquals(reachable, result);

        verify(deviceRepository).findAll();
        verify(connectionRepository).findAll();
        verify(graphBuilder).buildGraph(devices, connections);
        verify(reachabilityCalculator).calculateReachable(
                sourceDeviceId, graph, activeMap
        );
    }

    @Test
    void givenNoDevices_whenComputeReachableFrom_thenEmptyResultIsReturned() {
        Long sourceDeviceId = 1L;

        when(deviceRepository.findAll()).thenReturn(List.of());
        when(connectionRepository.findAll()).thenReturn(List.of());

        when(graphBuilder.buildGraph(List.of(), List.of()))
                .thenReturn(Map.of());

        when(reachabilityCalculator.calculateReachable(
                eq(sourceDeviceId),
                eq(Map.of()),
                eq(Map.of())
        )).thenReturn(Set.of());

        Set<Long> result = reachabilityService.computeReachableFrom(sourceDeviceId);

        assertEquals(Set.of(), result);
    }

    @Test
    void givenPreviousAndCurrentReachable_whenComputeDelta_thenAddedAndRemovedAreCorrect() {
        Set<Long> previous = Set.of(1L, 2L, 3L);
        Set<Long> current = Set.of(2L, 3L, 4L);

        DeltaDevices delta = reachabilityService.computeDelta(previous, current);

        assertEquals(Set.of(4L), delta.getAdded());
        assertEquals(Set.of(1L), delta.getRemoved());
    }

    @Test
    void givenIdenticalReachableSets_whenComputeDelta_thenDeltaIsEmpty() {
        Set<Long> reachable = Set.of(1L, 2L);

        DeltaDevices delta = reachabilityService.computeDelta(reachable, reachable);

        assertTrue(delta.isEmpty());
    }

    @Test
    void givenEmptyPrevious_whenComputeDelta_thenAllCurrentAreAdded() {
        Set<Long> previous = Set.of();
        Set<Long> current = Set.of(1L, 2L);

        DeltaDevices delta = reachabilityService.computeDelta(previous, current);

        assertEquals(Set.of(1L, 2L), delta.getAdded());
        assertEquals(Set.of(), delta.getRemoved());
    }
}
