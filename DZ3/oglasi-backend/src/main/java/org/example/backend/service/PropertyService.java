package org.example.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.backend.model.Ad;
import org.example.backend.model.Property;
import org.example.backend.dto.AdDTO;
import org.example.backend.repository.PropertyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PropertyService {

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
                    p.setPropertyValue(entry.getValue());
                    p.setIdAd(ad);
                    return p;
                })
                .toList();

    }

    @Transactional
    List<Property> updateAdProperties(Ad adFromDB, AdDTO newAd) {
        propertyRepository.deletePropertiesByIdAd(adFromDB);
        return saveMultipleProperties(parseProperties(newAd.getSpecs(), adFromDB));
    }

    List<Property> getPropertiesByAd(Ad ad) {
        return propertyRepository.getPropertyByIdAd(ad);
    }


}
