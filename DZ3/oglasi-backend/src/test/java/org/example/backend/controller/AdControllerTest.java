package org.example.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.backend.dto.AdDTO;
import org.example.backend.model.Ad;
import org.example.backend.model.City;
import org.example.backend.service.AdService;
import org.example.backend.service.CityService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdController.class)
public class AdControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdService adService;

    @Test
    void shouldCreateAd() throws Exception {
        Ad ad = new Ad();
        ad.setTitle("Ad 0");
        ad.setShortDesc("Short description");
        ad.setLongDesc("Long description");
        ad.setPrice(100.0f);
        ad.setAdType(1);
        ad.setState(1);
        ad.setCity(new City(1L, "Test City"));
        ad.setUploadTime(Instant.now());

        Mockito.when(adService.saveAd(Mockito.any(AdDTO.class)))
                .thenReturn(ad);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String adJson = objectMapper.writeValueAsString(new AdDTO(ad));

        mockMvc.perform(post("/ad/createAd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(adJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Ad 0"));
    }

    @Test
    void shouldUpdateAdd() throws Exception {
        Ad ad = new Ad();
        ad.setTitle("Ad 1");
        ad.setShortDesc("Short description");
        ad.setLongDesc("Long description");
        ad.setPrice(100.0f);
        ad.setAdType(1);
        ad.setState(1);
        ad.setCity(new City(1L, "Test City"));

        Mockito.when(adService.updateAd(Mockito.any(AdDTO.class)))
                .thenReturn(new AdDTO(ad));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String adJson = objectMapper.writeValueAsString(new AdDTO(ad));

        mockMvc.perform(patch("/ad/updateAd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(adJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Ad 1"));
    }
}
