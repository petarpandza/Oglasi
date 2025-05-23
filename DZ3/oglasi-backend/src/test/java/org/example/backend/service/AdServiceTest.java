package org.example.backend.service;

import org.example.backend.dto.AdDTO;
import org.example.backend.model.Ad;
import org.example.backend.model.User;
import org.example.backend.repository.AdRepository;
import org.example.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AdServiceTest {

    @Mock
    private AdRepository adRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ImageService imageService;

    @Mock
    private PropertyService propertyService;

    @InjectMocks
    private AdService adService;

    @Test
    void shouldReturnAllAds() {

        List<Ad> ads = List.of(
                new Ad(1, "Bicikl", "Bicikl koristen", null, 150.0f, 1, 2, Instant.now(), null, null),
                new Ad(2, "iPhone 14", "Razbijeno staklo", "Baterija 75%", 200.0f, 2, 1, Instant.now(), null, null),
                new Ad(3, "Farbanje zidova", "", "", 50.0f, 1, 1, Instant.now(), null, null),
                new Ad(4, "Instrukcije iz matematike", "Trazim nekoga da mi pomogne s matematikom", "", 20.0f, 2, 1, Instant.now(), null, null)
        );
        Mockito.when(adRepository.findAll()).thenReturn(ads);

        List<AdDTO> result = adService.getAllAds();

        assertEquals(4, result.size());
        assertEquals("Bicikl", result.get(0).getTitle());
        assertEquals(20.0, result.get(3).getPrice());

    }

    @Test
    void shouldSaveAd() {
        Ad ad = new Ad();
        ad.setId(1);
        ad.setTitle("Bicikl");
        ad.setShortDesc("Bicikl koristen");
        ad.setLongDesc(null);
        ad.setPrice(150.0f);
        ad.setAdType(1);
        ad.setState(2);
        ad.setUploadTime(Instant.now());

        AdDTO adDTO = new AdDTO(ad);

        Mockito.when(adRepository.save(Mockito.any())).thenReturn(ad);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        Mockito.when(imageService.parseImages(Mockito.any(), Mockito.any())).thenReturn(List.of());
        Mockito.when(propertyService.parseProperties(Mockito.any(), Mockito.any())).thenReturn(List.of());

        Ad result = adService.saveAd(adDTO);

        assertEquals("Bicikl", result.getTitle());
    }

}
