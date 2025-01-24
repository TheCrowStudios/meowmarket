package com.thecrowstudios.meowmarket;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthFormController {
    @GetMapping("/register")
    public String getRegister(Model model) {
        model.addAttribute("userRegistrationDTO", new UserRegistrationDTO());
        return "register";
    }
}
