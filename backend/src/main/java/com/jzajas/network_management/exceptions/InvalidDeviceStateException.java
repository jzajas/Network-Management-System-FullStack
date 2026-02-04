package com.jzajas.network_management.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InvalidDeviceStateException extends RuntimeException{
    private Long id;
    private boolean isActive;
}
