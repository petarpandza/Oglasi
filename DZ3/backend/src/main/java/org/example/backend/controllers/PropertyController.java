package org.example.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.example.backend.beans.Ad;
import org.example.backend.beans.Property;
import org.example.backend.services.PropertyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyRepository propertyRepository;

    Property saveProperty(Property property) {
        return propertyRepository.save(property);
    }

    List<Property> saveMultipleProperties(List<Property> properties) {
        return propertyRepository.saveAll(properties);
    }

    List<Property> parseProperties(Map<String, String> properties, Ad ad) {
        return properties.entrySet().stream()
                .map(entry -> {
                    Property p = new Property();
                    p.setPropertyName(entry.getKey());
                    p.setValue(entry.getValue());
                    p.setIdAd(ad);
                    return p;
                })
                .toList();

    }


}
