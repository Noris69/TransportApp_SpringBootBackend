package com.transport.repository;

import com.transport.model.SellerOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SellerOfferRepository extends JpaRepository<SellerOffer, Long> {
    List<SellerOffer> findByStatus(String status);

    // Nouvelle méthode pour récupérer les offres par email
    List<SellerOffer> findBySellerEmail(String email);
}
