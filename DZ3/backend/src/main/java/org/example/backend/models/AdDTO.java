package org.example.backend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdDTO {
    private Long id;
    private String name;
    private String description;
    private String city;
    private String type;
    private String state;
    private double price;
    private Map<String, String> specs;
    private List<String> pictures;

    @Override
    public String toString() {
        return  "id: " + id +
                "\nname: " + name +
                "\ndescription: " + description +
                "\ncity: " + city +
                "\ntype: " + type +
                "\nstate: " + state +
                "\nprice: " + price +
                "\nspecs: " + specs +
                "\npictures: " + pictures;
    }
}
