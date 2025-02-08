package com.thecrowstudios.meowmarket.authentication;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class UserService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // @Autowired
    // private PersistentTokenBasedRememberMeServices rememberMeServices;

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public User registerNewUser(UserRegistrationDTO userRegistrationDTO) {
        if (userRepository.existsByUsername(userRegistrationDTO.getUsername()))
            throw new RuntimeException("Username is already in use");
        if (userRepository.existsByEmail(userRegistrationDTO.getEmail()))
            throw new RuntimeException("Email is already in use");

        User user = new User();
        user.setUsername(userRegistrationDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        user.setEmail(userRegistrationDTO.getEmail());
        Set<String> auths = new HashSet<>();
        auths.add("ROLE_USER");
        user.setAuthorities(auths);

        return userRepository.save(user);
    }

    public void login(UserLoginDTO userLoginDTO,HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail(), userLoginDTO.getPassword()));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        RememberMeAuthenticationToken rememberMeToken = new RememberMeAuthenticationToken("nigger", authentication.getPrincipal(), authentication.getAuthorities());
        // rememberMeServices.loginSuccess(request, response, rememberMeToken);
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }

    public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            System.out.println("Authenticated user name: " + username);
            return userRepository.findByUsername(username).orElse(null);
        }

        System.out.println("No authenticated user found");
        return null;
    }

    public boolean isAdmin() {
         User user = getUser();
         return user != null && user.getAuthorities().stream()
                 .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }

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
