package com.jzajas.network_management.controllers;

import com.jzajas.network_management.dtos.PatchDeviceDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalTime;


@RestController
@RequestMapping("/devices")
@RequiredArgsConstructor
public class DeviceController {
    private static final String UPDATE_MESSAGE = "Update Successful";


//    @GetMapping("/{id}/reachable-devices ")
//    public ResponseEntity<InitialStateDTO> getReachableDevicesForDeviceById() {
//
//    }

    @GetMapping("/stream-sse")
    public Flux<ServerSentEvent<String>> streamEvents() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> ServerSentEvent.<String> builder()
                        .id(String.valueOf(sequence))
                        .event("periodic-event")
                        .data("SSE - " + LocalTime.now().toString())
                        .build());
    }


    @PatchMapping("/{id}")
    public ResponseEntity<String> patchDeviceById(
            @PathVariable Long id,
            @RequestBody PatchDeviceDTO dto
    ) {

        return new ResponseEntity(UPDATE_MESSAGE, HttpStatus.OK);
    }
}
