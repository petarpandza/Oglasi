package org.example.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.example.backend.beans.City;
import org.example.backend.services.CityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityController {

    private final CityRepository cityRepository;

    public List<String> getCities(){
        return cityRepository.findAll()
                .stream()
                .map(City::getName)
                .toList();
    }
}
