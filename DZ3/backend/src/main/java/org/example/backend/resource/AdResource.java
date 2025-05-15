package org.example.backend.resource;

import lombok.RequiredArgsConstructor;
import org.example.backend.controllers.AdController;
import org.example.backend.models.AdDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AdResource {

    private final AdController adController;

    @PostMapping("/createAd")
    public ResponseEntity<AdDTO> createAd(@RequestBody AdDTO ad){
        //TODO SAVE TO DATABASE DON'T FORGET TO EDIT RETURN VALUE
        System.out.println(ad);
        return ResponseEntity.ok(new AdDTO());
    }
}
