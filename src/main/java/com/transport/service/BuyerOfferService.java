package com.transport.service;

import com.transport.model.BuyerOffer;
import com.transport.repository.BuyerOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuyerOfferService {
    private final BuyerOfferRepository buyerOfferRepository;

    public BuyerOffer createBuyerOffer(BuyerOffer offer) {
        return buyerOfferRepository.save(offer);
    }

    public List<BuyerOffer> getOpenBuyerOffers() {
        return buyerOfferRepository.findByStatus("OPEN");
    }

    // Nouvelle méthode pour récupérer les offres du client connecté
    public List<BuyerOffer> getOffersByBuyerEmail(String email) {
        return buyerOfferRepository.findByBuyerEmail(email);
    }
}
