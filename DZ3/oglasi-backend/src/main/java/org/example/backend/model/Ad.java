package org.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "ad", schema = "core")
@AllArgsConstructor
@NoArgsConstructor
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ad", nullable = false)
    private Integer id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "short_desc", length = 1000)
    private String shortDesc;

    @Column(name = "long_desc", length = 5000)
    private String longDesc;

    @Column(name = "price")
    private Float price;

    @Column(name = "ad_type", nullable = false)
    private Integer adType;

    @Column(name = "state", nullable = false)
    private Integer state;

    @ColumnDefault("CURRENT_DATE")
    @Column(name = "upload_time", nullable = false)
    private Instant uploadTime;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_user", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User idUser;

    @JsonIgnoreProperties({"id_city"})
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_city", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private City city;

}