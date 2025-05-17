package org.example.backend.repository;

import org.example.backend.model.Ad;
import org.example.backend.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> getPropertyByIdAd(Ad idAd);

    void deletePropertiesByIdAd(Ad idAd);
}
