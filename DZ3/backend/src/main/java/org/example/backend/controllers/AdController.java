package org.example.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.example.backend.beans.*;
import org.example.backend.models.AdDTO;
import org.example.backend.services.AdRepository;
import org.example.backend.services.CityRepository;
import org.example.backend.services.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdController {

    private final AdRepository adRepository;
    private final CityRepository cityRepository;
    private final PropertyController propertyController;
    private final UserRepository userRepository;
    private final ImageController imageController;

    public Ad saveAd(AdDTO adDTO) {
        Ad ad = new Ad();

        //hard coded user
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("User not found"));
        ad.setIdUser(user);

        ad.setTitle(adDTO.getName());
        ad.setLongDesc(adDTO.getDescription());

        Optional<City> city = Optional.ofNullable(cityRepository.findByName(adDTO.getCity()).orElseThrow(() -> new RuntimeException("City not found")));
        city.ifPresent(ad::setIdCity);

        ad.setAdType(adDTO.getType());
        ad.setState(adDTO.getState());
        ad.setPrice((float) adDTO.getPrice());
        ad.setUploadTime(Instant.now());

        Ad savedAd = adRepository.save(ad);

        List<Image> createdImages = imageController.parseImages(adDTO.getPictures(), savedAd);
        imageController.saveMultipleImages(createdImages);

        List<Property> createdProperties = propertyController.parseProperties(adDTO.getSpecs(), savedAd);
        propertyController.saveMultipleProperties(createdProperties);

        //potential to create an adResponseDTO class to respond to the creation request

        return savedAd;

    }
}
