package com.aksa.chatapp.service;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionManager {

    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public void addSession(String username, WebSocketSession session) {
        sessions.put(username, session);
    }

    public WebSocketSession getSession(String username) {
        return sessions.get(username);
    }

    public void removeSession(String username) {
        sessions.remove(username);
    }
}
