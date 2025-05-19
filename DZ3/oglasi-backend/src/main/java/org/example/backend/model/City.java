package org.example.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Entity
@Getter
@Table(name = "city", schema = "core")
@AllArgsConstructor
@NoArgsConstructor
public class City {
    @Id
    private Long id_city;
    @Column(name = "name")
    private String name;
}
