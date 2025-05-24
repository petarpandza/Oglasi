package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.model.*;
import org.example.backend.dto.AdDTO;
import org.example.backend.repository.AdRepository;
import org.example.backend.repository.CityRepository;
import org.example.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdService {

    private final AdRepository adRepository;
    private final PropertyService propertyService;
    private final UserRepository userRepository;
    private final ImageService imageService;

    public Ad saveAd(AdDTO adDTO) {
        Ad ad = new Ad();

        //hard coded user
        User user = userRepository.findAll().get(0);
        ad.setIdUser(user);

        ad.setTitle(adDTO.getTitle());
        ad.setShortDesc(adDTO.getShortDesc());
        ad.setLongDesc(adDTO.getLongDesc());
        ad.setCity(adDTO.getCity());

        ad.setAdType(adDTO.getType());
        ad.setState(adDTO.getState());
        ad.setPrice((float) adDTO.getPrice());
        ad.setUploadTime(Instant.now());

        Ad savedAd = adRepository.save(ad);

        List<Image> createdImages = imageService.parseImages(adDTO.getPictures(), savedAd);
        imageService.saveMultipleImages(createdImages);

        List<Property> createdProperties = propertyService.parseProperties(adDTO.getSpecs(), savedAd);
        propertyService.saveMultipleProperties(createdProperties);

        return savedAd;

    }

    public List<AdDTO> getAllAds() {
        return adRepository.findAll().stream()
                .map(AdDTO::new)
                .toList();
    }

    public List<AdDTO> getUserAds(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return adRepository.findByIdUser(user).stream()
                .map(AdDTO::new)
                .toList();
    }

    public AdDTO getAdById(Long adId) {
        Optional<Ad> ad = adRepository.findById(adId);
        if (ad.isEmpty()) {
            return new AdDTO();
        }
        AdDTO realAd = new AdDTO(ad.get());
        Map<String, String> specs = new HashMap<>();
        for (Property p : propertyService.getPropertiesByAd(ad.get())) {
            specs.put(p.getPropertyName(), p.getPropertyValue());
        }
        List<String> pictures = imageService.getImagesByAd(ad.get()).stream()
                .map(Image::getImageData)
                .toList();
        realAd.setSpecs(specs);
        realAd.setPictures(pictures);

        return realAd;
    }

    public AdDTO updateAd(AdDTO adDTO) {
        Optional<Ad> ad = adRepository.findById(Long.valueOf(adDTO.getId()));
        if (ad.isPresent()) {
            Ad adFromDB = ad.get();
            adFromDB.setTitle(adDTO.getTitle());
            adFromDB.setShortDesc(adDTO.getShortDesc());
            adFromDB.setLongDesc(adDTO.getLongDesc());
            adFromDB.setPrice((float) adDTO.getPrice());
            adFromDB.setAdType(adDTO.getType());
            adFromDB.setState(adDTO.getState());
            adFromDB.setCity(adDTO.getCity());

            AdDTO toReturn = new AdDTO(adRepository.save(adFromDB));

            Map<String, String> specs = new HashMap<>();
            for (Property p : propertyService.updateAdProperties(adFromDB, adDTO)) {
                specs.put(p.getPropertyName(), p.getPropertyValue());
            }
            List<String> pictures = imageService.updateAdImages(adFromDB, adDTO).stream()
                    .map(Image::getImageData)
                    .toList();
            toReturn.setSpecs(specs);
            toReturn.setPictures(pictures);
            return toReturn;

        } else {
            return new AdDTO();
        }
    }

    public void deleteAd(Long adId) {
        Optional<Ad> ad = adRepository.findById(adId);
        ad.ifPresent(adRepository::delete);
    }
}
