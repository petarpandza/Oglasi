package org.example.backend.repository;

import org.example.backend.model.Ad;
import org.example.backend.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> getImageByIdAd(Ad idAd);

    void deleteImageByIdAd(Ad idAd);
}
