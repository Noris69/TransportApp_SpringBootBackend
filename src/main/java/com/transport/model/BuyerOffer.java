package com.transport.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "buyer_offers")
public class BuyerOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;

    private String cargoType; // Type de marchandise (ex: "General Food")
    private String truckType; // Type de camion demandé (ex: "Medium")
    private Double requiredDimension; // Dimensions demandées (en m³)

    private String pickupLocation;
    private String destinationLocation;
    private LocalDateTime pickupDate;

    private String paymentType; // ex: Fixed price, Request quote
    private String status = "OPEN"; // Status: OPEN, CLOSED, etc.
}
