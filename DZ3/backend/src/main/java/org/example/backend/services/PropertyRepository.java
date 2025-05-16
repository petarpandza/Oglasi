package org.example.backend.services;

import org.example.backend.beans.Ad;
import org.example.backend.beans.Property;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> getPropertyByIdAd(Ad idAd);
}
