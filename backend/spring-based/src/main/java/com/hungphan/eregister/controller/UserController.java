package com.hungphan.eregister.controller;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    
    private static final String CLIENT_SESSION_ID_NAME = "CLIENT_SESSION_ID";

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    
    @GetMapping("/check-authentication")
    private boolean isAuthenticated() {
        return true;
    }
    
    @GetMapping("/get-client-session-id")
    private void getClientSessionId(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (CLIENT_SESSION_ID_NAME.equals(cookie.getName())) return;
            }
        }
        String sessionId = UUID.randomUUID().toString();
        LOGGER.info("Client session id: {}", sessionId);
        Cookie cookie = new Cookie(CLIENT_SESSION_ID_NAME, sessionId);
        response.addCookie(cookie);
    }
    
}
