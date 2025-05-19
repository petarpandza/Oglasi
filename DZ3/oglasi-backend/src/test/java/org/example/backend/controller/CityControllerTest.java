package org.example.backend.controller;

import org.example.backend.model.City;
import org.example.backend.service.CityService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CityController.class)
public class CityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CityService cityService;

    @Test
    void shouldReturnAllCities() throws Exception {
        int citiesCount = 496;
        List<City> cities = new ArrayList<>();
        for (int i = 0; i < citiesCount; i++) {
            City city = new City();
            city.setId_city((long) i);
            city.setName("City " + i);
            cities.add(city);
        }
        Mockito.when(cityService.getCities()).thenReturn(cities);

        mockMvc.perform(get("/city/getCities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id_city").value(0))
                .andExpect(jsonPath("$[0].name").value("City 0"));
    }

}
