//package com.aksa.chatapp.controller;
//
//import com.aksa.chatapp.model.User;
//import com.aksa.chatapp.repository.UserRepository;
//import com.aksa.chatapp.security.JwtTokenUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/auth")
//public class AuthController {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private JwtTokenUtil jwtTokenUtil;
//
//    @PostMapping("/register")
//    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
//        String username = body.get("username");
//        String password = body.get("password");
//        String displayName = body.getOrDefault("displayName", username);
//
//        Map<String, Object> response = new HashMap<>();
//
//        if (username == null || username.trim().length() == 0 ||
//                password == null || password.trim().length() == 0) {
//            response.put("error", "username and password are required");
//            return ResponseEntity.badRequest().body(response);
//        }
//
//        if (userRepository.findByUsername(username).isPresent()) {
//            response.put("error", "username already exists");
//            return ResponseEntity.badRequest().body(response);
//        }
//
//        User user = new User(username, passwordEncoder.encode(password), displayName);
//        userRepository.save(user);
//
//        response.put("status", "ok");
//        response.put("message", "User registered successfully");
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
//        String username = body.get("username");
//        String password = body.get("password");
//
//        Map<String, Object> response = new HashMap<>();
//
//        if (username == null || username.trim().length() == 0 ||
//                password == null || password.trim().length() == 0) {
//            response.put("error", "username and password are required");
//            return ResponseEntity.badRequest().body(response);
//        }
//        Optional<User> optionalUser = userRepository.findByUsername(username);
//        if (!optionalUser.isPresent()) {
//            response.put("error", "invalid credentials");
//            return ResponseEntity.status(401).body(response);
//        }
//
//        User user = optionalUser.get();
//
//        if (!passwordEncoder.matches(password, user.getPassword())) {
//            response.put("error", "invalid credentials");
//            return ResponseEntity.status(401).body(response);
//        }
//
//        String token = jwtTokenUtil.generateToken(username);
//
//        response.put("token", token);
//        response.put("username", username);
//        response.put("displayName", user.getDisplayName());
//        response.put("message", "Login successful");
//
//        return ResponseEntity.ok(response);
//    }
//}
package com.aksa.chatapp.controller;

import com.aksa.chatapp.model.User;
import com.aksa.chatapp.repository.UserRepository;
import com.aksa.chatapp.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        String displayName = body.getOrDefault("displayName", username);

        Map<String, Object> response = new HashMap<>();

        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            response.put("error", "username and password are required");
            return ResponseEntity.badRequest().body(response);
        }

        if (userRepository.findByUsername(username).isPresent()) {
            response.put("error", "username already exists");
            return ResponseEntity.badRequest().body(response);
        }

        User user = new User(username, passwordEncoder.encode(password), displayName);
        userRepository.save(user);

        response.put("status", "ok");
        response.put("message", "User registered successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        Map<String, Object> response = new HashMap<>();

        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            response.put("error", "username and password are required");
            return ResponseEntity.badRequest().body(response);
        }

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (!optionalUser.isPresent()) {
            response.put("error", "invalid credentials");
            return ResponseEntity.status(401).body(response);
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            response.put("error", "invalid credentials");
            return ResponseEntity.status(401).body(response);
        }

        String token = jwtTokenUtil.generateToken(username);

        response.put("id", user.getId());
        response.put("token", token);
        response.put("username", username);
        response.put("displayName", user.getDisplayName());
        response.put("message", "Login successful");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyToken(Authentication auth) {
        String username = auth.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("displayName", user.getDisplayName());
        response.put("valid", true);

        return ResponseEntity.ok(response);
    }
}
