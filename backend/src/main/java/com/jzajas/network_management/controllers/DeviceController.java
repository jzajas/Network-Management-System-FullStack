package com.jzajas.network_management.controllers;

import com.jzajas.network_management.dtos.PatchDeviceDTO;
import com.jzajas.network_management.services.DeviceService;
import com.jzajas.network_management.services.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@RestController
@RequestMapping("/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final SubscriptionService subscriptionService;
    private final DeviceService deviceService;

    @GetMapping(value = "/{id}/reachable-devices", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamReachableDevices(@PathVariable Long id) {
        return subscriptionService.subscribe(id);
    }

    @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> patchDeviceById(
            @PathVariable Long id,
            @Valid @RequestBody PatchDeviceDTO dto
    ) {
        String message = deviceService.patchDevice(id, dto);

        return new ResponseEntity(message, HttpStatus.OK);
    }
}
