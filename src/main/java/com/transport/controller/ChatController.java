package com.transport.controller;

import com.transport.dto.ChatMessageRequest;
import com.transport.model.Message;
import com.transport.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestBody ChatMessageRequest chatMessageRequest) {
        if (chatMessageRequest.getSenderId() == null || chatMessageRequest.getRecipientId() == null
                || chatMessageRequest.getContent() == null || chatMessageRequest.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("All fields (senderId, recipientId, content) are required");
        }

        return ResponseEntity.ok(chatService.sendMessage(
                chatMessageRequest.getSenderId(),
                chatMessageRequest.getRecipientId(),
                chatMessageRequest.getContent()
        ));
    }



    @GetMapping("/history")
    public ResponseEntity<List<Message>> getChatHistory(
            @RequestParam(required = true) Long userId1,
            @RequestParam(required = true) Long userId2) {
        if (userId1 == null || userId2 == null) {
            throw new IllegalArgumentException("Both userId1 and userId2 are required");
        }
        List<Message> messages = chatService.getChatHistory(userId1, userId2);
        return ResponseEntity.ok(messages);
    }

}
