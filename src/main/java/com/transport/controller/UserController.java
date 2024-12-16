package com.transport.controller;

import com.transport.model.TransporterStatus;
import com.transport.model.User;
import com.transport.model.UserRole;
import com.transport.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // get client
    @GetMapping("/buyers")
    public ResponseEntity<List<User>> getBuyers() {
        return ResponseEntity.ok(userService.findByRole(UserRole.BUYER));
    }
    // get transporter
    @GetMapping("/sellers")
    public ResponseEntity<List<User>> getSellers() {
        return ResponseEntity.ok(userService.findByRole(UserRole.SELLER));
    }

    // get transporters by status
    @GetMapping("/sellers/status")
    public ResponseEntity<List<User>> getSellersByStatus(@RequestParam String status) {
        TransporterStatus transporterStatus;
        try {
            transporterStatus = TransporterStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // Retourne 400 si le statut est invalide
        }
        List<User> sellers = userService.findSellersByStatus(transporterStatus);
        return ResponseEntity.ok(sellers);
    }

    // get user by id

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(user);
    }

    // Seller edit status

    @PutMapping("/sellers/status")
    public ResponseEntity<User> updateSellerStatus(@RequestBody StatusUpdateRequest request, Authentication authentication) {
        User currentUser = userService.findByEmail(authentication.getName());

        // Vérifiez que l'utilisateur est un seller
        if (!currentUser.getRole().equals(UserRole.SELLER)) {
            return ResponseEntity.status(403).build(); // Accès interdit
        }

        // Mettre à jour le statut
        currentUser.setStatus(TransporterStatus.valueOf(request.getStatus().toUpperCase()));
        userService.saveUser(currentUser);

        return ResponseEntity.ok(currentUser);
    }
    // edit user info
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedData, Authentication authentication) {
        User currentUser = userService.findByEmail(authentication.getName());
        User existingUser = userService.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        // Vérifier les droits
        if (!currentUser.getRole().equals(UserRole.ADMIN) && !existingUser.getId().equals(currentUser.getId())) {
            return ResponseEntity.status(403).build();
        }

        User updatedUser = userService.updateUser(existingUser, updatedData);
        return ResponseEntity.ok(updatedUser);
    }

    //delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id, Authentication authentication) {
        User currentUser = userService.findByEmail(authentication.getName());
        User userToDelete = userService.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        // Vérifier les droits
        if (!currentUser.getRole().equals(UserRole.ADMIN) && !userToDelete.getId().equals(currentUser.getId())) {
            return ResponseEntity.status(403).build();
        }

        userService.deleteUser(userToDelete);
        return ResponseEntity.noContent().build();
    }

    // get user profile
    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        return ResponseEntity.ok(user);
    }

    // update user profile
    @PutMapping("/profile")
    public ResponseEntity<User> updateProfile(@RequestBody User updatedData, Authentication authentication) {
        User currentUser = userService.findByEmail(authentication.getName());
        User updatedUser = userService.updateUser(currentUser, updatedData);
        return ResponseEntity.ok(updatedUser);
    }

    @Data
    static class StatusUpdateRequest {
        private String status;
    }
}
