package com.thecrowstudios.meowmarket.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Valid
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute UserLoginDTO userLoginDTO, BindingResult bindingResult, Model model) {
        try {
            userService.login(userLoginDTO);
            return "redirect:/";
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
            model.addAttribute("error", "Incorrect email or password");
            return "login";
        }
    }

    @Valid
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute UserRegistrationDTO userRegistrationDTO,
            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getFieldErrors());
            return "register";
        }
        try {
            userService.registerNewUser(userRegistrationDTO);
            System.out.println("user registered");
            return "redirect:/";
        } catch (Exception e) {
            System.out.println("could not register user");
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @PostMapping("/logout")
    public String logout() {
        userService.logout();
        return "redirect:/";
    }
}
