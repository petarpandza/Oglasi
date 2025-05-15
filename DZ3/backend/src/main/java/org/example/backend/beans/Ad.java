package org.example.backend.beans;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
@Entity
@Table(name = "ad", schema = "core")
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ad", nullable = false)
    private Integer id;

    @Column(name = "title", nullable = false, length = 200)
    private String name;

    @Column(name = "long_desc", length = 5000)
    private String description;

    @Column(name = "price")
    private Float price;

    @Column(name = "ad_type", nullable = false)
    private Integer adType;

    @Column(name = "upload_time", nullable = false)
    private Instant uploadTime;

    @Column(name = "state", nullable = false)
    private Integer state;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_city", nullable = false)
    private City idCity;

    @ElementCollection
    private Map<String, String> specs;

    @ElementCollection
    private List<String> pictures;

}