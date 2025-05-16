package org.example.backend.services;

import org.example.backend.beans.Ad;
import org.example.backend.beans.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {
    List<Ad> findByIdUser(User idUser);
}
