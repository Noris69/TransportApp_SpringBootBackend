package com.transport.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.transport.dto.ChatMessageRequest;
import com.transport.model.Message;
import com.transport.service.ChatService;
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
public class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatService chatService;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    @WithMockUser(username = "user@example.com")
    public void testSendMessage() throws Exception {
        // Mock message data
        ChatMessageRequest request = new ChatMessageRequest();
        request.setSenderId(1L);
        request.setRecipientId(2L);
        request.setContent("Hello!");

        Message message = new Message();
        message.setId(1L);
        message.setSender(null); // Mock sender
        message.setRecipient(null); // Mock recipient
        message.setContent("Hello!");
        message.setTimestamp(LocalDateTime.now());

        Mockito.when(chatService.sendMessage(Mockito.eq(1L), Mockito.eq(2L), Mockito.eq("Hello!"))).thenReturn(message);

        // Execute the test
        mockMvc.perform(post("/api/chat/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("Hello!"));

        // Verify service interaction
        Mockito.verify(chatService, Mockito.times(1)).sendMessage(1L, 2L, "Hello!");
    }

    @Test
    @WithMockUser(username = "user@example.com")
    public void testGetChatHistory() throws Exception {
        // Mock chat history
        Message message1 = new Message();
        message1.setId(1L);
        message1.setContent("Hello!");
        message1.setTimestamp(LocalDateTime.now());

        Message message2 = new Message();
        message2.setId(2L);
        message2.setContent("Hi there!");
        message2.setTimestamp(LocalDateTime.now());

        List<Message> chatHistory = Arrays.asList(message1, message2);

        Mockito.when(chatService.getChatHistory(1L, 2L)).thenReturn(chatHistory);

        // Execute the test
        mockMvc.perform(get("/api/chat/history")
                        .param("userId1", "1")
                        .param("userId2", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].content").value("Hello!"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].content").value("Hi there!"));

        // Verify service interaction
        Mockito.verify(chatService, Mockito.times(1)).getChatHistory(1L, 2L);
    }
}
