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
    
    public void login(UserLoginDTO userLoginDTO) {
        User user = userRepository.findByEmail(userLoginDTO.getEmail()).orElseThrow(() -> new RuntimeException("Incorrect email or password"));
        if (!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) throw new RuntimeException("Incorrect email or password"); // check if passwords match

        userRepository.login(httpSession.getId(), user.getId());
    }

    public Boolean loggedIn() {
        String session = httpSession.getId();

        User user = userRepository.findBySession(session).orElse(null); // TODO - use an exists or whatever its called
        if (user != null) return true;
        return false;
    }

    public void logout() {
        userRepository.clearSession(httpSession.getId());
    }

    public User getUser() {
        return userRepository.findBySession(httpSession.getId()).orElseThrow(() -> new RuntimeException("User is not authenticated"));
    }

    public boolean isAdmin() {
        try {
            User user = getUser();
            
            if (user.getRole() == UserRole.ADMIN) return true;
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
