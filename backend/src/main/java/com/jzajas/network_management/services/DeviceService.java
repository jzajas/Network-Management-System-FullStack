package com.jzajas.network_management.services;

import com.jzajas.network_management.dtos.PatchDeviceDTO;

public interface DeviceService {
    String patchDevice(Long id, PatchDeviceDTO dto);
}
