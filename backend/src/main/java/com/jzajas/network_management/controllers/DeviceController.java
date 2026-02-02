package com.jzajas.network_management.controllers;

import com.jzajas.network_management.dtos.PatchDeviceDTO;
import com.jzajas.network_management.services.DeviceServiceImplementation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    private final DeviceServiceImplementation deviceService;

    @GetMapping("/{id}/reachable-devices")
    public SseEmitter streamReachableDevices(@PathVariable Long id) {

        // TODO:
        // 1. Create a new SseEmitter (with timeout)
        // 2. Register the emitter in SubscriptionRegistry
        //    together with:
        //      - root device ID
        //      - initial empty reachable set
        // 3. Compute initial reachable devices using ReachabilityService
        // 4. Send exactly ONE "initial_state" SSE event
        // 5. Store reachable set as subscriber state
        // 6. Return emitter

        return new SseEmitter();
    }


    @PatchMapping("/{id}")
    public ResponseEntity<String> patchDeviceById(
            @PathVariable Long id,
            @Valid @RequestBody PatchDeviceDTO dto
    ) {
        // TODO:
        // 1. Validate input (e.g. request.active not null)
        // 2. Delegate to application service:
        //    devicePatchService.updateDeviceState(id, request.active)
        // 3. The service should:
        //    - load device
        //    - update active flag
        //    - persist change
        //    - publish DeviceStateChangedEvent (after commit)

        String message = deviceService.patchDevice(id, dto);

        return new ResponseEntity("UPDATE_MESSAGE", HttpStatus.OK);
    }
}
