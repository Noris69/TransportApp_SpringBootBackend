package com.transport.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transport_requests")
public class TransportRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String pickupLocation;
    private String dropoffLocation;
    private LocalDateTime requestTime;
    private String status;
    
    @Column(columnDefinition = "decimal(10,6)")
    private Double pickupLatitude;
    
    @Column(columnDefinition = "decimal(10,6)")
    private Double pickupLongitude;
    
    @Column(columnDefinition = "decimal(10,6)")
    private Double dropoffLatitude;
    
    @Column(columnDefinition = "decimal(10,6)")
    private Double dropoffLongitude;
}