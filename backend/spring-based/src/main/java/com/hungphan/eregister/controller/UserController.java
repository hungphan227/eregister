package com.hungphan.eregister.controller;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    
    private static final String CLIENT_SESSION_ID_NAME = "CLIENT_SESSION_ID";
    
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
        Cookie cookie = new Cookie(CLIENT_SESSION_ID_NAME, UUID.randomUUID().toString());
        response.addCookie(cookie);
    }
    
}
