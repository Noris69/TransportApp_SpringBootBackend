package com.transport.service;

import com.transport.model.SellerOffer;
import com.transport.repository.SellerOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerOfferService {
    private final SellerOfferRepository sellerOfferRepository;

    public SellerOffer createSellerOffer(SellerOffer offer) {
        return sellerOfferRepository.save(offer);
    }

    public List<SellerOffer> getOpenSellerOffers() {
        return sellerOfferRepository.findByStatus("OPEN");
    }

    // Nouvelle méthode pour récupérer les offres du transporteur connecté
    public List<SellerOffer> getOffersBySellerEmail(String email) {
        return sellerOfferRepository.findBySellerEmail(email);
    }
}
