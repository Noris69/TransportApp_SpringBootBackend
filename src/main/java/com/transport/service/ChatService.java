package com.transport.service;

import com.transport.model.Message;
import com.transport.model.User;
import com.transport.repository.MessageRepository;
import com.transport.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public Message sendMessage(Long senderId, Long recipientId, String content) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new RuntimeException("Recipient not found"));

        Message message = new Message();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setContent(content);
        return messageRepository.save(message);
    }

    public List<Message> getChatHistory(Long userId1, Long userId2) {
        User user1 = userRepository.findById(userId1)
                .orElseThrow(() -> new RuntimeException("User1 not found"));
        User user2 = userRepository.findById(userId2)
                .orElseThrow(() -> new RuntimeException("User2 not found"));

        return messageRepository.findBySenderOrRecipient(user1, user2)
                .stream()
                .filter(message ->
                        (message.getSender().equals(user1) && message.getRecipient().equals(user2)) ||
                                (message.getSender().equals(user2) && message.getRecipient().equals(user1))
                )
                .toList();
    }

}
