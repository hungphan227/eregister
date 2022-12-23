package com.hungphan.eregister;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hungphan.eregister.dto.Jwt;

import java.util.Base64;

public class Utils {
    
    private static ObjectMapper objectMapper = new ObjectMapper();
    
//    public static String getCurrentUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (!(authentication instanceof AnonymousAuthenticationToken)) {
//            String currentUserName = authentication.getName();
//            return currentUserName;
//        }
//        return null;
//    }
    
    public static String convertFromObjectToJson(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

    public static Jwt decodeJwt(String token) throws Exception {
        String[] chunks = token.substring(7).split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String header = new String(decoder.decode(chunks[0]));
        String payload = new String(decoder.decode(chunks[1]));
        return objectMapper.readValue(payload, Jwt.class);
    }
}
