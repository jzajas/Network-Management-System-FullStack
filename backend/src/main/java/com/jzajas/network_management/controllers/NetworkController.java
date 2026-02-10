package com.jzajas.network_management.controllers;

import com.jzajas.network_management.dtos.NetworkTopologyDTO;
import com.jzajas.network_management.services.NetworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/network")
@RequiredArgsConstructor
public class NetworkController {

   private final NetworkService networkService;

    @GetMapping("topology")
    public ResponseEntity<NetworkTopologyDTO> getTopology() {
        NetworkTopologyDTO dto = networkService.getNetworkTopology();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
