package com.hungphan.studentapplication;

import java.security.Principal;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import reactor.core.publisher.Mono;

public class Utils {
    
    public static String getCurrentUser() {
        Object principal = ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal).block(Duration.of(1, ChronoUnit.DAYS));
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return currentUserName;
        }
        return null;
    }

}
