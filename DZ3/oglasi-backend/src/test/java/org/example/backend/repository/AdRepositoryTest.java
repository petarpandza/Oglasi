package org.example.backend.repository;

import org.example.backend.model.Ad;
import org.example.backend.model.City;
import org.example.backend.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.Instant;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class AdRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AdRepository adRepository;

    @Test
    void shouldFindAdsForUser1() {
        entityManager.clear();

        User user1 = new User(null, "user1", "password1", false, LocalDate.now(), new LinkedHashSet<>());
        User user2 = new User(null, "user2", "password2", false, LocalDate.now(), new LinkedHashSet<>());

        entityManager.persist(user1);
        entityManager.persist(user2);

        City zagreb = new City(1L, "Zagreb");
        entityManager.persist(zagreb);

        List<Ad> ads = List.of(
                new Ad(null, "Bicikl", "Bicikl koristen", null, 150.0f, 1, 2, Instant.now(), user1, zagreb),
                new Ad(null, "iPhone 14", "Razbijeno staklo", "Baterija 75%", 200.0f, 2, 1, Instant.now(), user1, zagreb),
                new Ad(null, "Farbanje zidova", "", "", 50.0f, 1, 1, Instant.now(), user2, zagreb),
                new Ad(null, "Instrukcije iz matematike", "Trazim nekoga da mi pomogne s matematikom", "", 20.0f, 2, 1, Instant.now(), user2, zagreb)
        );

        for (Ad ad : ads) {
            entityManager.persist(ad);
        }

        entityManager.flush();

        List<Ad> foundAds = adRepository.findByIdUser(user1);

        assertEquals(2, foundAds.size());
        assertEquals("Bicikl", foundAds.get(0).getTitle());
    }

    @Test
    void shouldDeleteAd() {
        entityManager.clear();

        User user1 = new User(null, "user1", "password1", false, LocalDate.now(), new LinkedHashSet<>());
        User user2 = new User(null, "user2", "password2", false, LocalDate.now(), new LinkedHashSet<>());

        entityManager.persist(user1);
        entityManager.persist(user2);

        City zagreb = new City(1L, "Zagreb");
        entityManager.persist(zagreb);

        List<Ad> ads = List.of(
                new Ad(null, "Bicikl", "Bicikl koristen", null, 150.0f, 1, 2, Instant.now(), user1, zagreb),
                new Ad(null, "iPhone 14", "Razbijeno staklo", "Baterija 75%", 200.0f, 2, 1, Instant.now(), user1, zagreb),
                new Ad(null, "Farbanje zidova", "", "", 50.0f, 1, 1, Instant.now(), user2, zagreb),
                new Ad(null, "Instrukcije iz matematike", "Trazim nekoga da mi pomogne s matematikom", "", 20.0f, 2, 1, Instant.now(), user2, zagreb)
        );

        for (Ad ad : ads) {
            entityManager.persist(ad);
        }

        entityManager.flush();

        entityManager.remove(ads.get(0));
        entityManager.flush();

        List<Ad> foundAds = adRepository.findByIdUser(user1);
        assertEquals(1, foundAds.size());
        assertEquals("iPhone 14", foundAds.get(0).getTitle());
    }

    @Test
    void shouldCreateAd() {
        entityManager.clear();

        User user1 = new User(null, "user1", "password1", false, LocalDate.now(), new LinkedHashSet<>());
        entityManager.persist(user1);
        City zagreb = new City(1L, "Zagreb");
        entityManager.persist(zagreb);

        Ad ad = new Ad(null, "Bicikl", "Bicikl koristen", null, 150.0f, 1, 2, Instant.now(), user1, zagreb);
        Ad savedAd = adRepository.save(ad);

        assertEquals(ad.getTitle(), savedAd.getTitle());
        assertEquals(ad.getShortDesc(), savedAd.getShortDesc());
    }

    @Test
    void shouldUpdateAd() {
        entityManager.clear();

        User user1 = new User(null, "user1", "password1", false, LocalDate.now(), new LinkedHashSet<>());
        entityManager.persist(user1);
        City zagreb = new City(1L, "Zagreb");
        entityManager.persist(zagreb);

        Ad ad = new Ad(null, "Bicikl", "Bicikl koristen", null, 150.0f, 1, 2, Instant.now(), user1, zagreb);
        Ad savedAd = entityManager.persist(ad);

        savedAd.setTitle("Updated Title");
        Ad updatedAd = adRepository.save(savedAd);

        assertEquals("Updated Title", updatedAd.getTitle());
    }

}
