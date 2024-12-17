package com.transport.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.transport.model.BuyerOffer;
import com.transport.service.BuyerOfferService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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
public class BuyerOfferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BuyerOfferService buyerOfferService;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    @WithMockUser(username = "buyer@example.com", roles = {"BUYER"})
    public void testCreateBuyerOffer() throws Exception {
        // Mock offer data
        BuyerOffer offer = new BuyerOffer();
        offer.setId(1L);
        offer.setCargoType("General Food");
        offer.setTruckType("Medium");
        offer.setRequiredDimension(20.5);
        offer.setPickupLocation("City A");
        offer.setDestinationLocation("City B");
        offer.setPickupDate(LocalDateTime.of(2024, 12, 20, 10, 0));
        offer.setPaymentType("Fixed price");
        offer.setStatus("OPEN");

        Mockito.when(buyerOfferService.createBuyerOffer(Mockito.any(BuyerOffer.class))).thenReturn(offer);

        // Execute the test
        mockMvc.perform(post("/api/buyer-offers/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(offer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cargoType").value("General Food"))
                .andExpect(jsonPath("$.truckType").value("Medium"))
                .andExpect(jsonPath("$.requiredDimension").value(20.5))
                .andExpect(jsonPath("$.pickupLocation").value("City A"))
                .andExpect(jsonPath("$.destinationLocation").value("City B"))
                .andExpect(jsonPath("$.pickupDate").value("2024-12-20T10:00:00"))
                .andExpect(jsonPath("$.paymentType").value("Fixed price"))
                .andExpect(jsonPath("$.status").value("OPEN"));

        // Verify service interaction
        Mockito.verify(buyerOfferService, Mockito.times(1)).createBuyerOffer(Mockito.any(BuyerOffer.class));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    public void testGetOpenBuyerOffers() throws Exception {
        // Mock open offers
        BuyerOffer offer1 = new BuyerOffer();
        offer1.setId(1L);
        offer1.setCargoType("General Food");
        offer1.setStatus("OPEN");
        BuyerOffer offer2 = new BuyerOffer();
        offer2.setId(2L);
        offer2.setCargoType("Electronics");
        offer2.setStatus("OPEN");

        List<BuyerOffer> offers = Arrays.asList(offer1, offer2);

        Mockito.when(buyerOfferService.getOpenBuyerOffers()).thenReturn(offers);

        // Execute the test
        mockMvc.perform(get("/api/buyer-offers/open")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].cargoType").value("General Food"))
                .andExpect(jsonPath("$[0].status").value("OPEN"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].cargoType").value("Electronics"))
                .andExpect(jsonPath("$[1].status").value("OPEN"));

        // Verify service interaction
        Mockito.verify(buyerOfferService, Mockito.times(1)).getOpenBuyerOffers();
    }

    @Test
    @WithMockUser(username = "buyer@example.com", roles = {"BUYER"})
    public void testGetMyBuyerOffers() throws Exception {
        // Mock offers for a specific user
        BuyerOffer offer1 = new BuyerOffer();
        offer1.setId(1L);
        offer1.setCargoType("General Food");
        offer1.setStatus("OPEN");
        BuyerOffer offer2 = new BuyerOffer();
        offer2.setId(2L);
        offer2.setCargoType("Electronics");
        offer2.setStatus("OPEN");

        List<BuyerOffer> myOffers = Arrays.asList(offer1, offer2);

        Mockito.when(buyerOfferService.getOffersByBuyerEmail("buyer@example.com")).thenReturn(myOffers);

        // Execute the test
        mockMvc.perform(get("/api/buyer-offers/my")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].cargoType").value("General Food"))
                .andExpect(jsonPath("$[0].status").value("OPEN"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].cargoType").value("Electronics"))
                .andExpect(jsonPath("$[1].status").value("OPEN"));

        // Verify service interaction
        Mockito.verify(buyerOfferService, Mockito.times(1)).getOffersByBuyerEmail("buyer@example.com");
    }
}
