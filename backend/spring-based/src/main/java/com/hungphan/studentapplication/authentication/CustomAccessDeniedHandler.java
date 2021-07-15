package com.hungphan.studentapplication.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * Used to handle forbidden access of authenticated user
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    
    public void handle(HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse,
                       AccessDeniedException e) throws IOException, ServletException {
        Authentication auth
                = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            System.out.println("User '" + auth.getName()
                    + "' attempted to access the protected URL: "
                    + httpServletRequest.getRequestURI());
        }

        httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
    }

}
