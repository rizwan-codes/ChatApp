    //package com.aksa.chatapp.controller;
    //
    //import com.aksa.chatapp.model.Message;
    //import com.aksa.chatapp.model.MessageDto;
    //import com.aksa.chatapp.model.User;
    //import com.aksa.chatapp.repository.MessageRepository;
    //import com.aksa.chatapp.repository.UserRepository;
    //import com.aksa.chatapp.service.NotificationService;
    //import org.springframework.beans.factory.annotation.Autowired;
    //import org.springframework.http.ResponseEntity;
    //import org.springframework.security.core.Authentication;
    //import org.springframework.web.bind.annotation.*;
    //
    //import java.util.List;
    //
    //@RestController
    //@RequestMapping("/api/messages")
    //public class MessageController {
    //
    //    @Autowired
    //    private MessageRepository messageRepository;
    //
    //    @Autowired
    //    private UserRepository userRepository;
    //
    //    @Autowired
    //    private NotificationService notificationService; // ✅ added
    //
    //    @PostMapping
    //    public ResponseEntity<?> sendMessage(@RequestBody MessageDto dto, Authentication auth) {
    //        String senderUsername = (String) auth.getPrincipal();
    //        User sender = userRepository.findByUsername(senderUsername)
    //                .orElseThrow(() -> new RuntimeException("Sender not found"));
    //
    //        User receiver = userRepository.findById(dto.getReceiverId())
    //                .orElseThrow(() -> new RuntimeException("Receiver not found"));
    //
    //        Message message = new Message();
    //        message.setSender(String.valueOf(sender));
    //        message.setReceiver(receiver);
    //        message.setContent(dto.getContent());
    //        messageRepository.save(message);
    //
    //        notificationService.createNotification(receiver, "New message from " + sender.getUsername());
    //
    //        return ResponseEntity.ok("Message sent successfully!");
    //    }
    //
    //    @GetMapping("/{receiverId}")
    //    public ResponseEntity<?> getMessages(@PathVariable Long receiverId, Authentication auth) {
    //        String username = (String) auth.getPrincipal();
    //
    //        User sender = userRepository.findByUsername(username)
    //                .orElseThrow(() -> new RuntimeException("Sender not found"));
    //
    //
    //        List<Message> messages = messageRepository.findChat(sender.getId(), receiverId);
    //        return ResponseEntity.ok(messages);
    //    }
    //}
    
    package com.aksa.chatapp.controller;
    
    import com.aksa.chatapp.model.Message;
    import com.aksa.chatapp.model.MessageDto;
    import com.aksa.chatapp.model.User;
    import com.aksa.chatapp.repository.MessageRepository;
    import com.aksa.chatapp.repository.UserRepository;
    import com.aksa.chatapp.service.NotificationService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.core.Authentication;
    import org.springframework.web.bind.annotation.*;
    
    import java.util.List;
    
    @RestController
    @RequestMapping("/api/messages")
    public class MessageController {
    
        @Autowired
        private MessageRepository messageRepository;
    
        @Autowired
        private UserRepository userRepository;
    
        @Autowired
       private NotificationService notificationService;
    
        @PostMapping
        public ResponseEntity<?> sendMessage(@RequestBody MessageDto dto, Authentication auth) {
            String senderUsername = auth.getName();
            User sender = userRepository.findByUsername(senderUsername)
                    .orElseThrow(() -> new RuntimeException("Sender not found"));
            User receiver = userRepository.findById(dto.getReceiverId())
                    .orElseThrow(() -> new RuntimeException("Receiver not found"));
    
            Message message = new Message();
            message.setSender(sender);
            message.setReceiver(receiver);
            message.setContent(dto.getContent());
            messageRepository.save(message);
            notificationService.createNotification(receiver, "New message from " + sender.getUsername());
    
            return ResponseEntity.ok("Message sent successfully");
        }
    
        @GetMapping("/{receiverId}")
        public ResponseEntity<?> getMessages(@PathVariable Long receiverId, Authentication auth) {
            String username = auth.getName();
            User sender = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Sender not found"));
    
            List<Message> messages = messageRepository.findChat(sender.getId(), receiverId);
            return ResponseEntity.ok(messages);
        }
    }
