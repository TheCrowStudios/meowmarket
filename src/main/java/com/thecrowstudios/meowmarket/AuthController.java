package com.thecrowstudios.meowmarket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Valid
    @PostMapping("/register")
    public ResponseEntity<?> postMethodName(@ModelAttribute UserRegistrationDTO userRegistrationDTO) {
        try {
            userService.registerNewUser(userRegistrationDTO);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
