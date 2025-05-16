package org.example.backend.beans;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

@Data
@Entity
@Getter
@Table(name = "city", schema = "core")
public class City {
    @Id
    private Long id_city;
    @Column(name = "name")
    private String name;
}
