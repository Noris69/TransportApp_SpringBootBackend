package com.transport.controller;

import com.transport.model.User;
import com.transport.model.UserRole;
import com.transport.service.LocationService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    // Classe interne pour représenter les données de localisation
    @Data
    static class NearbyRequest {
        private Double latitude;
        private Double longitude;
        private Double radiusKm = 5.0; // Par défaut, rayon de 5 km
    }

    @PostMapping("/nearby-buyers")
    public ResponseEntity<List<User>> getNearbyBuyers(@RequestBody NearbyRequest nearbyRequest) {
        if (nearbyRequest.getLatitude() == null || nearbyRequest.getLongitude() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        List<User> buyers = locationService.findUsersNearby(
                nearbyRequest.getLatitude(),
                nearbyRequest.getLongitude(),
                nearbyRequest.getRadiusKm(),
                UserRole.BUYER
        );
        return ResponseEntity.ok(buyers);
    }

    @PostMapping("/nearby-sellers")
    public ResponseEntity<List<User>> getNearbySellers(@RequestBody NearbyRequest nearbyRequest) {
        if (nearbyRequest.getLatitude() == null || nearbyRequest.getLongitude() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        List<User> sellers = locationService.findUsersNearby(
                nearbyRequest.getLatitude(),
                nearbyRequest.getLongitude(),
                nearbyRequest.getRadiusKm(),
                UserRole.SELLER
        );
        return ResponseEntity.ok(sellers);
    }
}
