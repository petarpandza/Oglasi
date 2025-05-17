package org.example.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.example.backend.beans.*;
import org.example.backend.models.AdDTO;
import org.example.backend.services.AdRepository;
import org.example.backend.services.CityRepository;
import org.example.backend.services.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        ad.setTitle(adDTO.getTitle());
        ad.setShortDesc(adDTO.getShortDesc());
        ad.setLongDesc(adDTO.getLongDesc());
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

        return savedAd;

    }

    public List<Ad> getUserAds(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return adRepository.findByIdUser(user);
    }

    public AdDTO getAdById(Long adId) {
        Optional<Ad> ad = adRepository.findById(adId);
        if (ad.isEmpty()) {
            return new AdDTO();
        }
        AdDTO realAd = new AdDTO(ad.get());
        Map<String, String> specs = new HashMap<>();
        for (Property p : propertyController.getPropertiesByAd(ad.get())) {
            specs.put(p.getPropertyName(), p.getValue());
        }
        List<String> pictures = imageController.getImagesByAd(ad.get()).stream()
                .map(Image::getImageData)
                .toList();
        realAd.setSpecs(specs);
        realAd.setPictures(pictures);

        return realAd;
    }

    public AdDTO updateAd(AdDTO adDTO){
        Optional<Ad> ad = adRepository.findById(Long.valueOf(adDTO.getId()));
        if(ad.isPresent()) {
            Ad adFromDB = ad.get();
            adFromDB.setTitle(adDTO.getTitle());
            adFromDB.setShortDesc(adDTO.getShortDesc());
            adFromDB.setLongDesc(adDTO.getLongDesc());
            adFromDB.setPrice((float) adDTO.getPrice());
            adFromDB.setAdType(adDTO.getType());
            adFromDB.setState(adDTO.getState());
            adFromDB.setIdCity(cityRepository.findByName(adDTO.getCity()).orElseThrow(() -> new RuntimeException("City not found")));

            AdDTO toReturn = new AdDTO(adRepository.save(adFromDB));

            Map<String, String> specs = new HashMap<>();
            for (Property p : propertyController.updateAdProperties(adFromDB, adDTO)) {
                specs.put(p.getPropertyName(), p.getValue());
            }
            List<String> pictures = imageController.updateAdImages(adFromDB, adDTO).stream()
                    .map(Image::getImageData)
                    .toList();
            toReturn.setSpecs(specs);
            toReturn.setPictures(pictures);
            return toReturn;

        } else {
            return new AdDTO();
        }
    }

    public void deleteAd(Long adId){
        Optional<Ad> ad = adRepository.findById(adId);
        ad.ifPresent(adRepository::delete);
    }
}
