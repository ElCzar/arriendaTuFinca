package com.gossip.arrienda_tu_finca;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.gossip.arrienda_tu_finca.security.JWTAuthorizationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JWTAuthorizationFilter jwtAuthorizationFilter;

    public SecurityConfig(JWTAuthorizationFilter jwtAuthorizationFilter) {
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> 
                auth
                    .requestMatchers("/api/v3/property/**").permitAll()
                    .requestMatchers("/api/v3/user/info/**").permitAll()
                    .requestMatchers("/api/v3/user/login").permitAll()
                    .requestMatchers("/api/v3/user/create").permitAll()
                    .requestMatchers("/api/v3/user/uploadPhoto/**").permitAll()
                    .requestMatchers("/api/v3/image/**").permitAll()
                    .requestMatchers("/api/v3/rental-requests/create/**").hasRole("RENTER")
                    .requestMatchers("/api/v3/rental-requests/host/**").hasRole("HOST")
                    .requestMatchers("/api/v3/rental-requests/renter/**").hasRole("RENTER")
                    .requestMatchers("/api/v3/rental-requests/property/**").hasRole("HOST")
                    .requestMatchers("/api/v3/rental-requests/cancel/**").hasRole("RENTER")
                    .requestMatchers("/api/v3/rental-requests/complete/**").hasRole("HOST")
                    .requestMatchers("/api/v3/rental-requests/reject/**").hasRole("HOST")
                    .requestMatchers("/api/v3/rental-requests/approve/**").hasRole("HOST")
                    .requestMatchers("/api/v3/rental-requests/pay/**").hasRole("RENTER")
                    .requestMatchers("/api/v3/rental-requests/review-property/**").hasRole("RENTER")
                    .requestMatchers("/api/v3/rental-requests/review-host/**").hasRole("RENTER")
                    .requestMatchers("/api/v3/rental-requests/review-renter/**").hasRole("HOST")
                    .requestMatchers("/api/v3/rental-requests/renter-comments/**").hasRole("HOST")
                    .requestMatchers("/api/v3/rental-requests/host-comments/**").hasRole("RENTER")
                    .requestMatchers("/api/v3/rental-requests/property-comments/**").hasRole("RENTER")
                    .requestMatchers("/property/**").permitAll()
                    .requestMatchers("/user/info/**").permitAll()
                    .requestMatchers("/user/login").permitAll()
                    .requestMatchers("/user/create").permitAll()
                    .requestMatchers("/user/uploadPhoto/**").permitAll()
                    .requestMatchers("/image/**").permitAll()
                    .requestMatchers("/rental-requests/create/**").hasRole("RENTER")
                    .requestMatchers("/rental-requests/host/**").hasRole("HOST")
                    .requestMatchers("/rental-requests/renter/**").hasRole("RENTER")
                    .requestMatchers("/rental-requests/property/**").hasRole("HOST")
                    .requestMatchers("/rental-requests/cancel/**").hasRole("RENTER")
                    .requestMatchers("/rental-requests/complete/**").hasRole("HOST")
                    .requestMatchers("/rental-requests/reject/**").hasRole("HOST")
                    .requestMatchers("/rental-requests/approve/**").hasRole("HOST")
                    .requestMatchers("/rental-requests/pay/**").hasRole("RENTER")
                    .requestMatchers("/rental-requests/review-property/**").hasRole("RENTER")
                    .requestMatchers("/rental-requests/review-host/**").hasRole("RENTER")
                    .requestMatchers("/rental-requests/review-renter/**").hasRole("HOST")
                    .requestMatchers("/rental-requests/renter-comments/**").hasRole("HOST")
                    .requestMatchers("/rental-requests/host-comments/**").hasRole("RENTER")
                    .requestMatchers("/rental-requests/property-comments/**").hasRole("RENTER")
                    .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:4200",
            "http://localhost:80",
            "http://10.43.100.118",
            "http://10.43.100.118:80",
            "http://10.43.100.118:4200",
            "http://arriendatufinca.local",
            "http://arriendatufinca.local:80",
            "http://arriendatufinca.local:4200",
            "http://front.local",
            "http://front.local:80",
            "http://front.local:4200"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManager.class);
    }
}