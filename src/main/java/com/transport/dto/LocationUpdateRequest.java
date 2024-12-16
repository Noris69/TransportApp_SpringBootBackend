package com.transport.dto;

import lombok.Data;

@Data
public class LocationUpdateRequest {
    private Long userId;
    private Double latitude;
    private Double longitude;
    private String status; // AVAILABLE, BUSY, OFFLINE
}