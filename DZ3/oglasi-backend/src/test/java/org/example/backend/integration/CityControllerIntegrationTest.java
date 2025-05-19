package org.example.backend.integration;

import org.example.backend.model.City;
import org.example.backend.repository.CityRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CityControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CityRepository cityRepository;

    @BeforeEach
    void setUp() {
        cityRepository.deleteAll();
        cityRepository.save(new City(1L, "Zagreb"));
        cityRepository.save(new City(2L, "Split"));
        cityRepository.save(new City(3L, "Rijeka"));
        cityRepository.save(new City(4L, "Osijek"));
    }

    @Test
    @Order(1)
    void shouldReturnAllCities() throws Exception {
        mockMvc.perform(get("/city/getCities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].name").value("Zagreb"))
                .andExpect(jsonPath("$[1].name").value("Split"))
                .andExpect(jsonPath("$[2].name").value("Rijeka"))
                .andExpect(jsonPath("$[3].name").value("Osijek"));
    }

}
