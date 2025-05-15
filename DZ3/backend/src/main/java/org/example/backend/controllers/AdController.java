package org.example.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.example.backend.beans.Ad;
import org.example.backend.services.AdRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdController {

    private final AdRepository adRepository;

    public Ad saveAd(Ad ad) {
        return adRepository.save(ad);
    }
}
