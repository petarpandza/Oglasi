package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.model.City;
import org.example.backend.repository.CityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;

    public List<String> getCities(){
        return cityRepository.findAll()
                .stream()
                .map(City::getName)
                .toList();
    }
}
