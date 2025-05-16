package org.example.backend.controllers;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.backend.beans.Ad;
import org.example.backend.beans.Image;
import org.example.backend.models.AdDTO;
import org.example.backend.services.ImageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageController {

    private final ImageRepository imageRepository;

    Image saveImage(Image image) {
        return imageRepository.save(image);
    }

    List<Image> saveMultipleImages(List<Image> images) {
        return imageRepository.saveAll(images);
    }

    List<Image> parseImages(List<String> images, Ad ad) {
        return images.stream()
                .map(entry -> {
                    Image i = new Image();
                    i.setImageData(entry);
                    i.setIdAd(ad);
                    return i;
                }).toList();
    }

    List<Image> getImagesByAd(Ad ad) {
        return imageRepository.getImageByIdAd((ad));
    }

    @Transactional
    List<Image> updateAdImages(Ad adFromDB, AdDTO newAd) {
        imageRepository.deleteImageByIdAd(adFromDB);
        return saveMultipleImages(parseImages(newAd.getPictures(), adFromDB));
    }

}
