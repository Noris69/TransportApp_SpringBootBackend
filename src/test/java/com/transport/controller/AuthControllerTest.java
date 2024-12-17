package com.transport.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transport.dto.LoginRequest;
import com.transport.dto.RegisterRequest;
import com.transport.model.User;
import com.transport.model.UserRole;
import com.transport.service.JwtService;
import com.transport.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AuthenticationProvider authenticationProvider;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testRegisterUser() throws Exception {
        // Mock request data
        RegisterRequest request = new RegisterRequest();
        request.setEmail("buyer@example.com");
        request.setPassword("password123");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPhoneNumber("123456789");
        request.setRole(UserRole.BUYER);

        // Mock user creation
        User user = new User();
        user.setId(1L);
        user.setEmail(request.getEmail());
        user.setRole(UserRole.BUYER);

        Mockito.when(userService.createUser(Mockito.any(User.class))).thenReturn(user);
        Mockito.when(jwtService.generateToken(user.getEmail())).thenReturn("mocked-jwt-token");

        // Execute the test
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"))
                .andExpect(jsonPath("$.email").value("buyer@example.com"))
                .andExpect(jsonPath("$.role").value("BUYER"));

        // Verify interactions
        Mockito.verify(userService, Mockito.times(1)).createUser(Mockito.any(User.class));
        Mockito.verify(jwtService, Mockito.times(1)).generateToken(Mockito.anyString());
    }

    @Test
    public void testLoginUser() throws Exception {
        // Mock request data
        LoginRequest request = new LoginRequest();
        request.setEmail("seller@example.com");
        request.setPassword("password123");

        // Mock user retrieval
        User user = new User();
        user.setEmail("seller@example.com");
        user.setRole(UserRole.SELLER);

        Mockito.when(userService.findByEmail(request.getEmail())).thenReturn(user);
        Mockito.when(jwtService.generateToken(user.getEmail())).thenReturn("mocked-jwt-token");
        Mockito.when(authenticationProvider.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));



        // Execute the test
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"))
                .andExpect(jsonPath("$.email").value("seller@example.com"))
                .andExpect(jsonPath("$.role").value("SELLER"));

        // Verify interactions
        Mockito.verify(userService, Mockito.times(1)).findByEmail(request.getEmail());
        Mockito.verify(jwtService, Mockito.times(1)).generateToken(Mockito.anyString());
    }
}
