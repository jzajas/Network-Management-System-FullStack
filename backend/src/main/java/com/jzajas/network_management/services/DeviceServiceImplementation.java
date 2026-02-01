package com.jzajas.network_management.services;

import com.jzajas.network_management.dtos.DeviceStateChangedEvent;
import com.jzajas.network_management.dtos.PatchDeviceDTO;
import com.jzajas.network_management.entities.Device;
import com.jzajas.network_management.events.EventPublisher;
import com.jzajas.network_management.repositories.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class DeviceServiceImplementation implements DeviceService {
    private static final String UPDATE_MESSAGE = "Update Successful";
    private static final String NOT_FOUND_ERROR_MESSAGE = "Device Not Found";
    private  static final String INVALID_STATUS_ERROR_MESSAGE = "Invalid Status";

    private final DeviceRepository deviceRepository;
    private final EventPublisher eventPublisher;


    @Override
    @Transactional
    public String patchDevice(Long id, PatchDeviceDTO dto) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(NOT_FOUND_ERROR_MESSAGE));

        boolean newStatus = dto.isActive();

        if (device.isActive() == newStatus) {
            throw new RuntimeException(INVALID_STATUS_ERROR_MESSAGE);
        }

        device.setActive(newStatus);
        deviceRepository.save(device);

        eventPublisher.publish(new DeviceStateChangedEvent(device.getId(), newStatus));

        return UPDATE_MESSAGE;
    }

}
