package com.transport.controller;

import com.transport.model.User;
import com.transport.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserLocationController {

    private final UserService userService;

    // Classe interne pour représenter les données de localisation
    @Data
    static class LocationUpdateRequest {
        private Double latitude;
        private Double longitude;
    }

    @PutMapping("/location")
    public ResponseEntity<String> updateLocation(@RequestBody LocationUpdateRequest locationRequest, Authentication authentication) {
        if (locationRequest.getLatitude() == null || locationRequest.getLongitude() == null) {
            return ResponseEntity.badRequest().body("Latitude and longitude are required");
        }
        User user = userService.findByEmail(authentication.getName());
        user.setLatitude(locationRequest.getLatitude());
        user.setLongitude(locationRequest.getLongitude());
        userService.saveUser(user);
        return ResponseEntity.ok("Location updated successfully");
    }
}
