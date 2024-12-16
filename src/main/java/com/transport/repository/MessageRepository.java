package com.transport.repository;

import com.transport.model.Message;
import com.transport.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderOrRecipient(User sender, User recipient);
}
