package com.transport.service;

import com.transport.model.User;
import com.transport.model.UserRole;
import com.transport.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final UserRepository userRepository;

    public List<User> findUsersNearby(Double latitude, Double longitude, Double radiusKm, UserRole role) {
        return userRepository.findAll().stream()
                .filter(user -> user.getLatitude() != null && user.getLongitude() != null)
                .filter(user -> user.getRole().equals(role))
                .filter(user -> calculateDistance(latitude, longitude, user.getLatitude(), user.getLongitude()) <= radiusKm)
                .collect(Collectors.toList());
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371; // Rayon de la Terre en km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }
}
