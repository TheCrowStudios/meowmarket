package com.thecrowstudios.meowmarket.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Loading user details for: " + username);
        User user = userRepository.findByEmail(username).orElseThrow(() ->
            new UsernameNotFoundException("User not found: " + username)
        );

        System.out.println("Found user: " + username);
        return user;
    }
}
