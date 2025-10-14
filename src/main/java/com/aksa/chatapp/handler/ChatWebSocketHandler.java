//package com.aksa.chatapp.handler;
//
//import com.aksa.chatapp.model.Message;
//import com.aksa.chatapp.service.MessageService;
//import com.aksa.chatapp.service.SessionManager;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.*;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.time.Instant;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//public class ChatWebSocketHandler extends TextWebSocketHandler {
//
//    @Autowired
//    private SessionManager sessionManager;
//
//    @Autowired
//    private MessageService messageService;
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        Map<String, Object> attrs = session.getAttributes();
//        String username = (String) attrs.get("username");
//
//        if (username == null) {
//            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("No username"));
//            return;
//        }
//
//        // Add the session to our singleton session manager
//        sessionManager.addSession(username, session);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("type", "connected");
//        response.put("username", username);
//
//        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
//    }
//
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payload = message.getPayload();
//
//        Map<String, String> map = objectMapper.readValue(payload, Map.class);
//        String sender = map.get("sender");
//        String recipient = map.get("recipient");
//        String content = map.get("content");
//
//        if (sender == null || recipient == null || content == null) {
//            Map<String, Object> error = new HashMap<>();
//            error.put("type", "error");
//            error.put("message", "Invalid payload");
//            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(error)));
//            return;
//        }
//
//        Message m = new Message();
//        messageService.saveMessageAndFlush(m);
//
//        Map<String, Object> toSend = new HashMap<>();
//        toSend.put("type", "message");
//        toSend.put("sender", sender);
//        toSend.put("recipient", recipient);
//        toSend.put("content", content);
//        toSend.put("sentAt", m.getSentAt().toString());
//
//        String json = objectMapper.writeValueAsString(toSend);
//
//        WebSocketSession recipientSession = sessionManager.getSession(recipient);
//        if (recipientSession != null && recipientSession.isOpen()) {
//            recipientSession.sendMessage(new TextMessage(json));
//            messageService.markDelivered(m.getId());
//        }
//
//        Map<String, Object> ack = new HashMap<>();
//        ack.put("type", "ack");
//        ack.put("id", m.getId());
//        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(ack)));
//    }
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        String username = (String) session.getAttributes().get("username");
//        if (username != null) {
//            sessionManager.removeSession(username);
//        }
//    }
//}

package com.aksa.chatapp.handler;

import com.aksa.chatapp.model.Message;
import com.aksa.chatapp.model.User;
import com.aksa.chatapp.repository.UserRepository;
import com.aksa.chatapp.service.MessageService;
import com.aksa.chatapp.service.SessionManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Map<String, Object> attrs = session.getAttributes();
        String username = (String) attrs.get("username");

        if (username == null) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("No username"));
            return;
        }

        sessionManager.addSession(username, session);

        Map<String, Object> response = new HashMap<>();
        response.put("type", "connected");
        response.put("username", username);

        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();

        Map<String, String> map = objectMapper.readValue(payload, Map.class);
        String sender = map.get("sender");
        String recipient = map.get("recipient");
        String content = map.get("content");

        if (sender == null || recipient == null || content == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("type", "error");
            error.put("message", "Invalid payload");
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(error)));
            return;
        }

        try {
            User senderUser = userRepository.findByUsername(sender)
                    .orElseThrow(() -> new RuntimeException("Sender not found: " + sender));
            User recipientUser = userRepository.findByUsername(recipient)
                    .orElseThrow(() -> new RuntimeException("Recipient not found: " + recipient));

            Message m = new Message();
            m.setSender(senderUser);
            m.setReceiver(recipientUser);
            m.setContent(content);
            m.setSentAt(Instant.now());
            m.setDelivered(false);

            messageService.saveMessageAndFlush(m);

            Map<String, Object> toSend = new HashMap<>();
            toSend.put("type", "message");
            toSend.put("sender", sender);
            toSend.put("recipient", recipient);
            toSend.put("content", content);
            toSend.put("sentAt", m.getSentAt().toString());

            String json = objectMapper.writeValueAsString(toSend);

            WebSocketSession recipientSession = sessionManager.getSession(recipient);
            if (recipientSession != null && recipientSession.isOpen()) {
                recipientSession.sendMessage(new TextMessage(json));
                messageService.markDelivered(m.getId());
            }

            Map<String, Object> ack = new HashMap<>();
            ack.put("type", "ack");
            ack.put("id", m.getId());
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(ack)));

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("type", "error");
            error.put("message", "Failed to send message: " + e.getMessage());
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(error)));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String username = (String) session.getAttributes().get("username");
        if (username != null) {
            sessionManager.removeSession(username);
        }
    }
}
