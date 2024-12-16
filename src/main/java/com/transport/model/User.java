package com.transport.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String firstName;
    private String lastName;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<TransportRequest> requests = new HashSet<>();

    // Ajout des coordonnées GPS
    private Double latitude;
    private Double longitude;

    // Statut pour les transporteurs (seulement pour les Sellers)
    @Enumerated(EnumType.STRING)
    private TransporterStatus status; // Par défaut null, sauf pour les Buyers où c'est AVAILABLE

}