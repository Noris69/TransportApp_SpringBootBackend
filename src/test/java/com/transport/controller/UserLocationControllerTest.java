package com.transport.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transport.model.User;
import com.transport.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserLocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser(username = "user@example.com")
    public void testUpdateLocation() throws Exception {
        // Mock user
        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        // Mock behavior
        Mockito.when(userService.findByEmail("user@example.com")).thenReturn(user);

        // Create request payload
        String locationUpdateRequest = "{" +
                "\"latitude\":37.7749," +
                "\"longitude\":-122.4194" +
                "}";

        // Execute the test
        mockMvc.perform(put("/api/users/location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(locationUpdateRequest))
                .andExpect(status().isOk())
                .andExpect(content().string("Location updated successfully"));

        // Verify service interactions
        Mockito.verify(userService, Mockito.times(1)).findByEmail("user@example.com");
        Mockito.verify(userService, Mockito.times(1)).saveUser(user);

        // Assert that the user's location was updated
        assert user.getLatitude().equals(37.7749);
        assert user.getLongitude().equals(-122.4194);
    }

    @Test
    @WithMockUser(username = "user@example.com")
    public void testUpdateLocationBadRequest() throws Exception {
        // Missing latitude and longitude
        String invalidRequest = "{}";

        // Execute the test
        mockMvc.perform(put("/api/users/location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Latitude and longitude are required"));

        // Verify no service interaction
        Mockito.verifyNoInteractions(userService);
    }
}
