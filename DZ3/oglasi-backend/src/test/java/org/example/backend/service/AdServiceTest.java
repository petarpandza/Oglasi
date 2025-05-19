package org.example.backend.service;

import org.example.backend.dto.AdDTO;
import org.example.backend.model.Ad;
import org.example.backend.repository.AdRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AdServiceTest {

    @Mock
    private AdRepository adRepository;

    @InjectMocks
    private AdService adService;

    @Test
    void shouldReturnAllAds() {

        List<Ad> ads = List.of(
                new Ad(1, "Bicikl", "Bicikl koristen", null, 150.0f, 1, 2, Instant.now(), null, null),
                new Ad(1, "iPhone 14", "Razbijeno staklo", "Baterija 75%", 200.0f, 2, 1, Instant.now(), null, null),
                new Ad(1, "Farbanje zidova", "", "", 50.0f, 1, 1, Instant.now(), null, null),
                new Ad(1, "Instrukcije iz matematike", "Trazim nekoga da mi pomogne s matematikom", "", 20.0f, 2, 1, Instant.now(), null, null)
        );
        Mockito.when(adRepository.findAll()).thenReturn(ads);

        List<AdDTO> result = adService.getAllAds();

        assertEquals(4, result.size());
        assertEquals("Bicikl", result.get(0).getTitle());
        assertEquals(20.0, result.get(3).getPrice());

    }

}
