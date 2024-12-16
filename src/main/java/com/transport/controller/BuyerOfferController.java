package com.transport.controller;

import com.transport.model.BuyerOffer;
import com.transport.service.BuyerOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buyer-offers")
@RequiredArgsConstructor
public class BuyerOfferController {
    private final BuyerOfferService buyerOfferService;

    @PostMapping("/create")
    public ResponseEntity<BuyerOffer> createBuyerOffer(@RequestBody BuyerOffer offer) {
        return ResponseEntity.ok(buyerOfferService.createBuyerOffer(offer));
    }

    @GetMapping("/open")
    public ResponseEntity<List<BuyerOffer>> getOpenBuyerOffers() {
        return ResponseEntity.ok(buyerOfferService.getOpenBuyerOffers());
    }

    // Nouvelle méthode pour récupérer les offres du client connecté
    @GetMapping("/my")
    public ResponseEntity<List<BuyerOffer>> getMyBuyerOffers(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(buyerOfferService.getOffersByBuyerEmail(email));
    }
}
