package com.transport.repository;

import com.transport.model.TransportRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransportRequestRepository extends JpaRepository<TransportRequest, Long> {
    List<TransportRequest> findByUserId(Long userId);
    List<TransportRequest> findByStatus(String status);
}