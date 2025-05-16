package org.example.backend.beans;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "property", schema = "core")
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_property", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_ad", nullable = false)
    private Ad idAd;

    @Column(name = "property_name", nullable = false)
    private String propertyName;

    @Column(name = "value", nullable = false)
    private String value;

}