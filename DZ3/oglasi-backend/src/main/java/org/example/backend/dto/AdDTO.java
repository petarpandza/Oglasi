package org.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.backend.model.Ad;
import org.example.backend.model.City;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdDTO {
    private Integer id;
    private String title;
    private String shortDesc;
    private String longDesc;
    private double price;
    private Integer type;
    private Integer state;
    private City city;
    private Instant uploadTime;
    private Map<String, String> specs;
    private List<String> pictures;

    public AdDTO(Ad ad) {
        this.id = ad.getId();
        this.title = ad.getTitle();
        this.shortDesc = ad.getShortDesc();
        this.longDesc = ad.getLongDesc();
        this.price = ad.getPrice();
        this.type = ad.getAdType();
        this.state = ad.getState();
        this.uploadTime = ad.getUploadTime();
        this.city = ad.getCity();
    }
}
