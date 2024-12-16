package com.transport.controller;

import com.transport.model.SellerOffer;
import com.transport.service.SellerOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seller-offers")
@RequiredArgsConstructor
public class SellerOfferController {
    private final SellerOfferService sellerOfferService;

    @PostMapping("/create")
    public ResponseEntity<SellerOffer> createSellerOffer(@RequestBody SellerOffer offer) {
        return ResponseEntity.ok(sellerOfferService.createSellerOffer(offer));
    }

    @GetMapping("/open")
    public ResponseEntity<List<SellerOffer>> getOpenSellerOffers() {
        return ResponseEntity.ok(sellerOfferService.getOpenSellerOffers());
    }

    // Nouvelle méthode pour récupérer les offres du transporteur connecté
    @GetMapping("/my")
    public ResponseEntity<List<SellerOffer>> getMySellerOffers(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(sellerOfferService.getOffersBySellerEmail(email));
    }
}
