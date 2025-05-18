package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.service.CityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CityController {

    private final CityService cityService;

    @GetMapping("/cities")
    public ResponseEntity<List<String>> getCities(){
        return ResponseEntity.ok(cityService.getCities());
    }
}
