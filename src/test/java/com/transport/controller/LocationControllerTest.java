package com.transport.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transport.model.User;
import com.transport.service.LocationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationService locationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser(username = "user@example.com", roles = {"BUYER"})
    public void testGetNearbyBuyers() throws Exception {
        // Mock nearby buyers
        User buyer1 = new User();
        buyer1.setId(1L);
        buyer1.setFirstName("John");
        buyer1.setLastName("Doe");

        User buyer2 = new User();
        buyer2.setId(2L);
        buyer2.setFirstName("Jane");
        buyer2.setLastName("Smith");

        List<User> buyers = Arrays.asList(buyer1, buyer2);

        Mockito.when(locationService.findUsersNearby(37.7749, -122.4194, 5.0, com.transport.model.UserRole.BUYER)).thenReturn(buyers);

        // Create request payload
        String requestPayload = "{" +
                "\"latitude\":37.7749," +
                "\"longitude\":-122.4194," +
                "\"radiusKm\":5.0" +
                "}";

        // Execute the test
        mockMvc.perform(post("/api/location/nearby-buyers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));

        // Verify service interaction
        Mockito.verify(locationService, Mockito.times(1)).findUsersNearby(37.7749, -122.4194, 5.0, com.transport.model.UserRole.BUYER);
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"SELLER"})
    public void testGetNearbySellers() throws Exception {
        // Mock nearby sellers
        User seller1 = new User();
        seller1.setId(1L);
        seller1.setFirstName("Alice");
        seller1.setLastName("Brown");

        User seller2 = new User();
        seller2.setId(2L);
        seller2.setFirstName("Bob");
        seller2.setLastName("White");

        List<User> sellers = Arrays.asList(seller1, seller2);

        Mockito.when(locationService.findUsersNearby(37.7749, -122.4194, 5.0, com.transport.model.UserRole.SELLER)).thenReturn(sellers);

        // Create request payload
        String requestPayload = "{" +
                "\"latitude\":37.7749," +
                "\"longitude\":-122.4194," +
                "\"radiusKm\":5.0" +
                "}";

        // Execute the test
        mockMvc.perform(post("/api/location/nearby-sellers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Alice"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].firstName").value("Bob"));

        // Verify service interaction
        Mockito.verify(locationService, Mockito.times(1)).findUsersNearby(37.7749, -122.4194, 5.0, com.transport.model.UserRole.SELLER);
    }
}
