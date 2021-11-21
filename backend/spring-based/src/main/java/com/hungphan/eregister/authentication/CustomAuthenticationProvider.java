package com.hungphan.eregister.authentication;

import java.util.Arrays;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.hungphan.eregister.model.Student;
import com.hungphan.eregister.repository.StudentRepository;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private StudentRepository studentRepository;
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String studentNumber = authentication.getName();
        String password = authentication.getCredentials().toString();

        Student student = studentRepository.findByStudentNumber(studentNumber);
        if (!Objects.isNull(student) && studentNumber.equals(student.getStudentNumber())) {
            return new UsernamePasswordAuthenticationToken(studentNumber, password,
                    Arrays.asList(new SimpleGrantedAuthority("USER")));
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
