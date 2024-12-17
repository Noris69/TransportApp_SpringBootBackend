package com.transport.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.transport.model.SellerOffer;
import com.transport.service.SellerOfferService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SellerOfferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SellerOfferService sellerOfferService;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    @WithMockUser(username = "seller@example.com", roles = {"SELLER"})
    public void testCreateSellerOffer() throws Exception {
        // Mock offer data
        SellerOffer offer = new SellerOffer();
        offer.setId(1L);
        offer.setTransportType("Truck");
        offer.setAvailableDimension(20.0);
        offer.setOriginLocation("City A");
        offer.setDestinationLocation("City B");
        offer.setPrice(1000.0);
        offer.setAvailableFromDate(LocalDateTime.now());
        offer.setAvailableToDate(LocalDateTime.now().plusDays(5));
        offer.setStatus("OPEN");

        Mockito.when(sellerOfferService.createSellerOffer(Mockito.any(SellerOffer.class))).thenReturn(offer);

        // Execute the test
        mockMvc.perform(post("/api/seller-offers/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(offer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.transportType").value("Truck"))
                .andExpect(jsonPath("$.originLocation").value("City A"))
                .andExpect(jsonPath("$.destinationLocation").value("City B"));

        // Verify service interaction
        Mockito.verify(sellerOfferService, Mockito.times(1)).createSellerOffer(Mockito.any(SellerOffer.class));
    }

    @Test
    @WithMockUser(username = "seller@example.com", roles = {"SELLER"})
    public void testGetOpenSellerOffers() throws Exception {
        // Mock open offers
        SellerOffer offer1 = new SellerOffer();
        offer1.setId(1L);
        offer1.setTransportType("Truck");

        SellerOffer offer2 = new SellerOffer();
        offer2.setId(2L);
        offer2.setTransportType("Van");

        List<SellerOffer> offers = Arrays.asList(offer1, offer2);

        Mockito.when(sellerOfferService.getOpenSellerOffers()).thenReturn(offers);

        // Execute the test
        mockMvc.perform(get("/api/seller-offers/open")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].transportType").value("Truck"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].transportType").value("Van"));

        // Verify service interaction
        Mockito.verify(sellerOfferService, Mockito.times(1)).getOpenSellerOffers();
    }

    @Test
    @WithMockUser(username = "seller@example.com", roles = {"SELLER"})
    public void testGetMySellerOffers() throws Exception {
        // Mock offers for a specific seller
        SellerOffer offer1 = new SellerOffer();
        offer1.setId(1L);
        offer1.setTransportType("Truck");

        SellerOffer offer2 = new SellerOffer();
        offer2.setId(2L);
        offer2.setTransportType("Van");

        List<SellerOffer> myOffers = Arrays.asList(offer1, offer2);

        Mockito.when(sellerOfferService.getOffersBySellerEmail("seller@example.com")).thenReturn(myOffers);

        // Mock authentication
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn("seller@example.com");

        // Execute the test
        mockMvc.perform(get("/api/seller-offers/my")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].transportType").value("Truck"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].transportType").value("Van"));

        // Verify service interaction
        Mockito.verify(sellerOfferService, Mockito.times(1)).getOffersBySellerEmail("seller@example.com");
    }
}
