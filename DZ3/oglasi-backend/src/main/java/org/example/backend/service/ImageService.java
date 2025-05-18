package org.example.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.backend.model.Ad;
import org.example.backend.model.Image;
import org.example.backend.dto.AdDTO;
import org.example.backend.repository.ImageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

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
