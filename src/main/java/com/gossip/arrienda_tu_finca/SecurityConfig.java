package com.gossip.arrienda_tu_finca;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()  // Allow access to all endpoints
            )
            .formLogin().disable()  // Disable the default login form
            .httpBasic().disable()  // Disable basic authentication
            .csrf().disable();  // Disable CSRF for testing or non-browser clients

        return http.build();
    }

}

