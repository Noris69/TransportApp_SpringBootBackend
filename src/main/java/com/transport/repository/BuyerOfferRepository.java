package com.transport.repository;

import com.transport.model.BuyerOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BuyerOfferRepository extends JpaRepository<BuyerOffer, Long> {
    List<BuyerOffer> findByStatus(String status);

    // Nouvelle méthode pour récupérer les offres par email
    List<BuyerOffer> findByBuyerEmail(String email);
}
