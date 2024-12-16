package com.transport.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "seller_offers")
public class SellerOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    private String transportType; // Type de transport proposé
    private Double availableDimension; // Dimensions disponibles (en m³)
    private String originLocation;
    private String destinationLocation;

    private Double price; // Prix proposé
    private LocalDateTime availableFromDate;
    private LocalDateTime availableToDate;

    private String status = "OPEN"; // Status: OPEN, ACCEPTED, etc.
}
