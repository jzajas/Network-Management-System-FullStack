package com.jzajas.network_management.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PatchDeviceDTO {

    @NotNull(message = "Status is required")
    private boolean active;
}
