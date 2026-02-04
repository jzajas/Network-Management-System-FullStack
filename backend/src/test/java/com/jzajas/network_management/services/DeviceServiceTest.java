package com.jzajas.network_management.services;

import com.jzajas.network_management.dtos.PatchDeviceDTO;
import com.jzajas.network_management.entities.Device;
import com.jzajas.network_management.events.DeviceStateChangedEvent;
import com.jzajas.network_management.events.EventPublisher;
import com.jzajas.network_management.repositories.DeviceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeviceServiceTest {
    private static final PatchDeviceDTO ACTIVATING_PATCH_DTO = new PatchDeviceDTO(true);
    private static final String SUCCESSFUL_UPDATE_MESSAGE = "Update Successful";
    private static final String NOT_FOUND_ERROR_MESSAGE = "Device Not Found";
    private  static final String INVALID_STATUS_ERROR_MESSAGE = "Invalid Status";
    private static final long DEFAULT_ID = 1L;
    private static final String DEFAULT_NAME = "Lublin";
    private static final Device DEFAULT_DEACTIVATED_DEVICE = new Device(DEFAULT_ID, DEFAULT_NAME, false);

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private DeviceServiceImplementation deviceService;

    @Captor
    ArgumentCaptor<DeviceStateChangedEvent> eventCaptor;

    @Test
    void givenExistingDeviceWithDifferentStatus_whenPatchDevice_thenDeviceIsUpdatedAndEventPublished() {
        when(deviceRepository.findById(DEFAULT_ID)).thenReturn(Optional.of(DEFAULT_DEACTIVATED_DEVICE));

        String result = deviceService.patchDevice(DEFAULT_ID, ACTIVATING_PATCH_DTO);

        assertEquals(SUCCESSFUL_UPDATE_MESSAGE, result);
        assertTrue(DEFAULT_DEACTIVATED_DEVICE.isActive());

        verify(deviceRepository).save(DEFAULT_DEACTIVATED_DEVICE);
        verify(eventPublisher).publish(eventCaptor.capture());

        DeviceStateChangedEvent event = eventCaptor.getValue();
        assertEquals(DEFAULT_ID, event.getDeviceId());
        assertTrue(event.isActive());
    }

    @Test
    void givenNonExistingDevice_whenPatchDevice_thenExceptionIsThrown() {
        when(deviceRepository.findById(DEFAULT_ID)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> deviceService.patchDevice(DEFAULT_ID, ACTIVATING_PATCH_DTO)
        );

        assertEquals(NOT_FOUND_ERROR_MESSAGE, exception.getMessage());
        verify(deviceRepository, never()).save(any());
        verify(eventPublisher, never()).publish(any());
    }

    @Test
    void givenDeviceWithSameStatus_whenPatchDevice_thenExceptionIsThrown() {
        PatchDeviceDTO dto = new PatchDeviceDTO(false);

        when(deviceRepository.findById(DEFAULT_ID)).thenReturn(Optional.of(DEFAULT_DEACTIVATED_DEVICE));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> deviceService.patchDevice(DEFAULT_ID, dto)
        );

        assertEquals(INVALID_STATUS_ERROR_MESSAGE, exception.getMessage());
        verify(deviceRepository, never()).save(any());
        verify(eventPublisher, never()).publish(any());
    }
}
