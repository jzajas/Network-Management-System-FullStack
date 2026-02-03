package com.jzajas.network_management.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PatchDeviceDTO {
    @NotNull(message = "Status is required")
    private boolean active;
}
