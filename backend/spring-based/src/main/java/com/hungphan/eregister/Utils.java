package com.hungphan.eregister;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {
    
    private static ObjectMapper objectMapper = new ObjectMapper();
    
    public static String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return currentUserName;
        }
        return null;
    }
    
    public static String convertFromObjectToJson(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

}
