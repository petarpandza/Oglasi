package org.example.backend.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        User user1 = userRepository.save(new User(null, "user1", "password1", false, LocalDate.now(), new LinkedHashSet<>()));
        User user2 = userRepository.save(new User(null, "user2", "password2", false, LocalDate.now(), new LinkedHashSet<>()));

        cityRepository.deleteAll();
        City zagreb = cityRepository.save(new City(1L, "Zagreb"));

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

    @Test
    @Order(3)
    void shouldDeleteAd() throws Exception {
        Ad adToDelete = adRepository.findAll().get(0);
        mockMvc.perform(delete("/ad/deleteAd/" + adToDelete.getId()))
                .andExpect(status().isNoContent());

        List<Ad> ads = adRepository.findAll();
        assertEquals(3, ads.size());
    }

    @Test
    @Order(4)
    void shouldCreateAd() throws Exception {
        cityRepository.save(new City(50L, "Test City"));
        String adJson = """
                {"id":0,"title":"Test Ad","shortDesc":"ShortDesc","longDesc":null,"city":{"id_city":50,"name":"Test City"},"type":"2","state":"1","price":100,"specs":{},"pictures":[]}
                """;

        mockMvc.perform(post("/ad/createAd")
                        .contentType("application/json")
                        .content(adJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Ad"));

        List<Ad> ads = adRepository.findAll();
        assertEquals(5, ads.size());
    }

    @Test
    @Order(5)
    void shouldUpdateAd() throws Exception {
        cityRepository.save(new City(50L, "Test City"));
        Ad adToUpdate = adRepository.findAll().get(0);
        String updatedAdJson = """
                {"id":%d,"title":"Updated Ad","shortDesc":"Updated Short Desc","longDesc":"Updated Long Desc","city":{"id_city":50,"name":"Test City"},"type":"2","state":"1","price":200,"specs":{},"pictures":[]}
                """.formatted(adToUpdate.getId());

        mockMvc.perform(patch("/ad/updateAd")
                        .contentType("application/json")
                        .content(updatedAdJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Ad"))
                .andExpect(jsonPath("$.shortDesc").value("Updated Short Desc"));

        Ad updatedAd = adRepository.findById((long)adToUpdate.getId()).orElseThrow();
        assertEquals("Updated Ad", updatedAd.getTitle());
        assertEquals("Updated Short Desc", updatedAd.getShortDesc());
        assertEquals("Updated Long Desc", updatedAd.getLongDesc());
    }

}
