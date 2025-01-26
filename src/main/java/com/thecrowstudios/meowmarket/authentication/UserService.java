package com.thecrowstudios.meowmarket.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private AddressRepository addressRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private HttpSession httpSession;

    public User registerNewUser(UserRegistrationDTO userRegistrationDTO) {
        if (userRepository.existsByUsername(userRegistrationDTO.getUsername())) throw new RuntimeException("Username is already in use");
        if (userRepository.existsByEmail(userRegistrationDTO.getEmail())) throw new RuntimeException("Email is already in use");

        User user = new User();
        user.setUsername(userRegistrationDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        user.setEmail(userRegistrationDTO.getEmail());
        user.setSession(httpSession.getId());
        user.setRole(UserRole.USER);

        return userRepository.save(user);
    }
    
    public User validLogin(UserLoginDTO userLoginDTO) {
        User user = userRepository.findByEmail(userLoginDTO.getEmail()).orElse(null);
        if (user == null) return null; // no user with email
        if (passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) return user; // passwords match
        
        return null;
    }

    public void login(User user) {
        userRepository.login(httpSession.getId(), user.getId());
    }

    public Boolean loggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser");
    }

    public Boolean loggedInSession() {
        String session = httpSession.getId();

        User user = userRepository.findBySession(session).orElse(null);
        if (user != null) return true;
        return false;
    }

    public void logout() {
        userRepository.clearSession(httpSession.getId());
    }
}
