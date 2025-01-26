package com.thecrowstudios.meowmarket.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthFormController {
    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String getLogin(Model model) {
        if (userService.loggedIn()) return "redirect:/";
        model.addAttribute("userLoginDTO", new UserLoginDTO());
        return "login";
    }

    @GetMapping("/register")
    public String getRegister(Model model) {
        model.addAttribute("userRegistrationDTO", new UserRegistrationDTO());
        return "register";
    }
}
