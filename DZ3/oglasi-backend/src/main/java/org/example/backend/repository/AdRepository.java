package org.example.backend.repository;

import org.example.backend.model.Ad;
import org.example.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {
    List<Ad> findByIdUser(User idUser);
}
