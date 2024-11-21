package com.gossip.arrienda_tu_finca.security;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gossip.arrienda_tu_finca.entities.User;
import com.gossip.arrienda_tu_finca.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userInfo) throws UsernameNotFoundException {
        // Extract the email from the userInfo string
        Pattern pattern = Pattern.compile("\"username\":\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(userInfo);
        if (!matcher.find()) {
            throw new UsernameNotFoundException("No email found in userInfo");
        }
        String email = matcher.group(1);

        User user = userRepository.findOptionalByEmail(email).orElseThrow(
            () -> new UsernameNotFoundException("User with email " + email + " not found")
        );

        if (user == null) {
            throw new UsernameNotFoundException("User not found on user details service");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        if (user.isHost()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_HOST"));
        }
        if (user.isRenter()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_RENTER"));
        }

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    public UserDetails loadByEmail(String email) {
        String userInfo = "{\"username\":\"" + email + "\"}";
        return loadUserByUsername(userInfo);
    }
}