package org.example.backend.integration;

import org.example.backend.model.Ad;
import org.example.backend.model.City;
import org.example.backend.model.User;
import org.example.backend.repository.AdRepository;
import org.example.backend.repository.CityRepository;
import org.example.backend.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        User user1 = new User(null, "user1", "password1", false, LocalDate.now(), new LinkedHashSet<>());
        User user2 = new User(null, "user2", "password2", false, LocalDate.now(), new LinkedHashSet<>());
        userRepository.save(user1);
        userRepository.save(user2);

        cityRepository.deleteAll();
        City zagreb = new City(1L, "Zagreb");
        cityRepository.save(zagreb);

        adRepository.deleteAll();
        List<Ad> ads = List.of(
                new Ad(null, "Bicikl", "Bicikl koristen", null, 150.0f, 1, 2, Instant.now(), user1, zagreb),
                new Ad(null, "iPhone 14", "Razbijeno staklo", "Baterija 75%", 200.0f, 2, 1, Instant.now(), user2, zagreb),
                new Ad(null, "Farbanje zidova", "", "", 50.0f, 1, 1, Instant.now(), user1, zagreb),
                new Ad(null, "Instrukcije iz matematike", "Trazim nekoga da mi pomogne s matematikom", "", 20.0f, 2, 1, Instant.now(), user2, zagreb)
        );
        adRepository.saveAll(ads);
    }

    @Test
    @Order(1)
    void shouldReturnAddsForUser1() throws Exception {
        mockMvc.perform(get("/ad/getAds/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Bicikl"))
                .andExpect(jsonPath("$[1].title").value("Farbanje zidova"));
    }

    @Test
    @Order(2)
    void shouldReturnAllAds() throws Exception {
        mockMvc.perform(get("/ad/getAllAds"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[2].title").value("Farbanje zidova"));
    }

}
