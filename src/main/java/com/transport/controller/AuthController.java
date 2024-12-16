package com.transport.controller;

import com.transport.dto.AuthResponse;
import com.transport.dto.LoginRequest;
import com.transport.dto.RegisterRequest;
import com.transport.model.User;
import com.transport.model.UserRole;
import com.transport.model.TransporterStatus;
import com.transport.service.JwtService;
import com.transport.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationProvider authenticationProvider;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(request.getRole());

        // Définir le statut par défaut en fonction du rôle
        if (request.getRole() == UserRole.SELLER) {
            user.setStatus(TransporterStatus.AVAILABLE);
        } else if (request.getRole() == UserRole.BUYER) {
            user.setStatus(null);
        }

        User createdUser = userService.createUser(user);
        String token = jwtService.generateToken(createdUser.getEmail());

        return ResponseEntity.ok(new AuthResponse(token, createdUser.getEmail(), createdUser.getRole().name()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
        authenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userService.findByEmail(request.getEmail());
        String token = jwtService.generateToken(user.getEmail());

        return ResponseEntity.ok(new AuthResponse(token, user.getEmail(), user.getRole().name()));
    }
}
