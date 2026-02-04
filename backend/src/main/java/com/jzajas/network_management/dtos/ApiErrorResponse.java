package com.jzajas.network_management.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class ApiErrorResponse {
    private String message;
    Instant time;
}