package com.aksa.chatapp.controller;

import com.aksa.chatapp.model.User;
import com.aksa.chatapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<?> getAllUsers(Authentication auth) {
        String currentUsername = auth.getName();

        List<User> allUsers = userRepository.findAll();
        List<Map<String, Object>> users = allUsers.stream()
                .filter(user -> !user.getUsername().equals(currentUsername))
                .map(user -> {
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("id", user.getId());
                    userMap.put("username", user.getUsername());
                    userMap.put("displayName", user.getDisplayName());
                    userMap.put("name", user.getDisplayName()); // For frontend compatibility
                    userMap.put("email", user.getUsername() + "@example.com"); // Placeholder
                    userMap.put("status", "ONLINE");
                    userMap.put("lastMessage", "");
                    userMap.put("unreadCount", 0);
                    return userMap;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.getOrDefault("password", "password123");
        String displayName = body.getOrDefault("displayName", username);

        Map<String, Object> response = new HashMap<>();

        if (username == null || username.trim().isEmpty()) {
            response.put("error", "Username is required");
            return ResponseEntity.badRequest().body(response);
        }

        if (userRepository.findByUsername(username).isPresent()) {
            response.put("error", "Username already exists");
            return ResponseEntity.badRequest().body(response);
        }

        User user = new User(username, passwordEncoder.encode(password), displayName);
        User savedUser = userRepository.save(user);

        Map<String, Object> userResponse = new HashMap<>();
        userResponse.put("id", savedUser.getId());
        userResponse.put("username", savedUser.getUsername());
        userResponse.put("displayName", savedUser.getDisplayName());
        userResponse.put("name", savedUser.getDisplayName());
        userResponse.put("email", savedUser.getUsername() + "@example.com");
        userResponse.put("status", "ONLINE");
        userResponse.put("lastMessage", "");
        userResponse.put("unreadCount", 0);

        return ResponseEntity.ok(userResponse);
    }
}