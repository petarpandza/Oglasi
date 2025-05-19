package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.AdDTO;
import org.example.backend.service.AdService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/ad")
public class AdController {

    private final AdService adService;

    @PostMapping("/createAd")
    public ResponseEntity<AdDTO> createAd(@RequestBody AdDTO adDTO) {
        return ResponseEntity.ok(new AdDTO(adService.saveAd(adDTO)));
    }

    @GetMapping("/getAds/{userId}")
    public ResponseEntity<List<AdDTO>> getAds(@PathVariable Long userId) {
        return ResponseEntity.ok(adService.getUserAds(userId));
    }

    @GetMapping("/getAd/{adId}")
    public ResponseEntity<AdDTO> getAdById(@PathVariable Long adId) {
        return ResponseEntity.ok(adService.getAdById(adId));
    }

    @PatchMapping("/updateAd")
    public ResponseEntity<AdDTO> updateAdById(@RequestBody AdDTO adDTO) {
        return ResponseEntity.ok(adService.updateAd(adDTO));
    }

    @DeleteMapping("/deleteAd/{adId}")
    public ResponseEntity<?> DeleteAdById(@PathVariable Long adId) {
        adService.deleteAd(adId);
        return ResponseEntity.noContent().build();
    }

}
