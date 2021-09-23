package com.hungphan.studentapplication.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    
    @GetMapping("/check-authentication")
    private boolean isAuthenticated() {
        return true;
    }
    
}
