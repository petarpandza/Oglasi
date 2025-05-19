package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.model.City;
import org.example.backend.service.CityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/city")
public class CityController {

    private final CityService cityService;

    @GetMapping("/getCities")
    public ResponseEntity<List<City>> getCities(){
        return ResponseEntity.ok(cityService.getCities());
    }
}
