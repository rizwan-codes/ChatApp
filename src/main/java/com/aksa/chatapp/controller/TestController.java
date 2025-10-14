package com.aksa.chatapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/server-test")
    public String testServer() {
        return "✅ ChatApp WAR deployment is working on Tomcat!";
    }
}
