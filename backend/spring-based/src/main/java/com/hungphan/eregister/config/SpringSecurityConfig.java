package com.hungphan.eregister.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationProvider authenticationProvider;
    
    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;
    
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;
    
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;
    
    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;

    // roles admin allow to access /admin/**
    // roles user allow to access /user/**
    // custom 403 access denied handler
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable().authorizeRequests().antMatchers("/", "/home", "/about").permitAll()
//                .antMatchers("/admin/**").hasAnyRole("ADMIN").antMatchers("/user/**").hasAnyRole("USER").anyRequest()
//                .authenticated().and().formLogin().loginPage("/login").permitAll().and().logout().permitAll();

        http.cors().configurationSource(corsConfigurationSource()).and().csrf().disable().authorizeRequests().antMatchers("/static/**").permitAll()
                .antMatchers("/*.json").permitAll()
                .antMatchers("/*.png").permitAll()
                .antMatchers("/get-client-session-id", "/course-registration", "/favicon.ico").permitAll()
                .anyRequest().authenticated().and().formLogin()
                .loginPage("/login").permitAll().successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler).and().exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint)
                .and().logout().logoutSuccessHandler(logoutSuccessHandler);
    }

    // create two users, admin and user
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        String encodedPassword = encoder.encode("1");
        auth.authenticationProvider(authenticationProvider);
    }

    private CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}
