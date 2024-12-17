package com.transport.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transport.model.TransporterStatus;
import com.transport.model.User;
import com.transport.model.UserRole;
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

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    public void testUpdateUser() throws Exception {
        User currentUser = new User();
        currentUser.setId(1L);
        currentUser.setEmail("admin@example.com");
        currentUser.setRole(UserRole.ADMIN);

        User existingUser = new User();
        existingUser.setId(2L);
        existingUser.setFirstName("John");

        User updatedUser = new User();
        updatedUser.setId(2L);
        updatedUser.setFirstName("Jonathan");

        Mockito.when(userService.findByEmail("admin@example.com")).thenReturn(currentUser);
        Mockito.when(userService.findById(2L)).thenReturn(Optional.of(existingUser));
        Mockito.when(userService.updateUser(Mockito.any(User.class), Mockito.any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jonathan"));

        Mockito.verify(userService, Mockito.times(1)).updateUser(existingUser, updatedUser);
    }

    @Test
    @WithMockUser(username = "seller@example.com", roles = {"SELLER"})
    public void testUpdateSellerStatus() throws Exception {
        User seller = new User();
        seller.setId(1L);
        seller.setEmail("seller@example.com");
        seller.setRole(UserRole.SELLER);
        seller.setStatus(TransporterStatus.AVAILABLE);

        Mockito.when(userService.findByEmail("seller@example.com")).thenReturn(seller);

        String statusUpdateRequest = "{\"status\":\"ON_MISSION\"}";

        mockMvc.perform(put("/api/users/sellers/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(statusUpdateRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ON_MISSION"));

        seller.setStatus(TransporterStatus.ON_MISSION);
        Mockito.verify(userService, Mockito.times(1)).saveUser(seller);
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    public void testDeleteUser() throws Exception {
        User currentUser = new User();
        currentUser.setId(1L);
        currentUser.setEmail("admin@example.com");
        currentUser.setRole(UserRole.ADMIN);

        User userToDelete = new User();
        userToDelete.setId(2L);

        Mockito.when(userService.findByEmail("admin@example.com")).thenReturn(currentUser);
        Mockito.when(userService.findById(2L)).thenReturn(Optional.of(userToDelete));

        mockMvc.perform(delete("/api/users/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Mockito.verify(userService, Mockito.times(1)).deleteUser(userToDelete);
    }
}
