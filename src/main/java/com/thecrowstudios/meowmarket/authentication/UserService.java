package com.thecrowstudios.meowmarket.authentication;

import java.util.Collection;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerNewUser(UserRegistrationDTO userRegistrationDTO) {
        if (userRepository.existsByUsername(userRegistrationDTO.getUsername()))
            throw new RuntimeException("Username is already in use");
        if (userRepository.existsByEmail(userRegistrationDTO.getEmail()))
            throw new RuntimeException("Email is already in use");

        User user = new User();
        user.setUsername(userRegistrationDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        user.setEmail(userRegistrationDTO.getEmail());
        user.setAuthorities("ROLE_USER");

        return userRepository.save(user);
    }

    public void login(UserLoginDTO userLoginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail(), userLoginDTO.getPassword()));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }

    public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            System.out.println("Authenticated user email: " + email);
            return userRepository.findByEmail(email).orElse(null);
        }

        System.out.println("No authenticated user found");
        return null;
    }

    // public boolean isAdmin() {
    //     User user = getUser();
    //     return user != null && user.getRole() == UserRole.ADMIN;
    // }

    public Collection<? extends GrantedAuthority> getCurrentUserAuthorities() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            System.out.println("Current authentication: " + authentication);
            System.out.println("Authentication authorities: " + authentication.getAuthorities());
            return authentication.getAuthorities();
        }
        return Collections.emptyList();
    }
}
