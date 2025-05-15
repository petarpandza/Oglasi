package org.example.backend.resource;

import lombok.RequiredArgsConstructor;
import org.example.backend.controllers.CityController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CityResource {

    private final CityController cityController;

    @GetMapping("/cities")
    public ResponseEntity<List<String>> getCities(){
        List<String> cities = cityController.getCities();
        return ResponseEntity.ok(cities);
    }
}
