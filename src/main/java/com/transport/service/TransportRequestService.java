package com.transport.service;

import com.transport.model.TransportRequest;
import com.transport.repository.TransportRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransportRequestService {
    private final TransportRequestRepository transportRequestRepository;

    public TransportRequest createRequest(TransportRequest request) {
        request.setRequestTime(LocalDateTime.now());
        request.setStatus("PENDING");
        return transportRequestRepository.save(request);
    }

    public List<TransportRequest> getRequestsByUser(Long userId) {
        return transportRequestRepository.findByUserId(userId);
    }

    public List<TransportRequest> getActiveRequests() {
        return transportRequestRepository.findByStatus("PENDING");
    }

    public TransportRequest updateRequestStatus(Long requestId, String status) {
        TransportRequest request = transportRequestRepository.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Request not found"));
        request.setStatus(status);
        return transportRequestRepository.save(request);
    }
}