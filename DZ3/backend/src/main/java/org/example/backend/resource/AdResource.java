package org.example.backend.resource;

import lombok.RequiredArgsConstructor;
import org.example.backend.beans.Ad;
import org.example.backend.controllers.AdController;
import org.example.backend.models.AdDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AdResource {

    private final AdController adController;

    @PostMapping("/createAd")
    public ResponseEntity<AdDTO> createAd(@RequestBody AdDTO adDTO) {
        Ad ad = adController.saveAd(adDTO);
        return ResponseEntity.ok(new AdDTO(ad));
    }

    @GetMapping("/getAds/{userId}")
    public ResponseEntity<List<AdDTO>> getAds(@PathVariable Long userId) {
        List<Ad> userAds = adController.getUserAds(userId);
        List<AdDTO> response = userAds.stream()
                .map(AdDTO::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAd/{adId}")
    public ResponseEntity<AdDTO> getAdById(@PathVariable Long adId) {
        return ResponseEntity.ok(adController.getAdById(adId));
    }

    //TODO MAKE DELETE AND UPDATE API CALLS
}
