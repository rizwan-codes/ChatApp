package com.aksa.chatapp.controller;

import com.aksa.chatapp.model.User;
import com.aksa.chatapp.service.NotificationService;
import com.aksa.chatapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> getMyNotifications(Authentication auth) {
        String username = auth.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(notificationService.getUnreadNotifications(user));
    }


    @PostMapping("/read/{id}")
    public ResponseEntity<?> markNotificationAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok("Marked as read");
    }

    @PutMapping("/read-all")
    public ResponseEntity<?> markAllNotificationsAsRead(Authentication auth) {
        String username = auth.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        notificationService.markAllAsRead(user);
        return ResponseEntity.ok("All notifications marked as read");
    }
}
