package com.aksa.chatapp.service;

import com.aksa.chatapp.model.Message;
import com.aksa.chatapp.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Transactional
    public Message saveMessageAndFlush(Message m) {
        Message saved = messageRepository.save(m);
        messageRepository.flush();
        return saved;
    }

    @Transactional
    public void markDelivered(Long messageId) {
        Optional<Message> opt = messageRepository.findById(messageId);
        if (opt.isPresent()) {
            Message m = opt.get();
            m.setDelivered(true);
            messageRepository.saveAndFlush(m); // ✅ combined save+flush
        }
    }
}
