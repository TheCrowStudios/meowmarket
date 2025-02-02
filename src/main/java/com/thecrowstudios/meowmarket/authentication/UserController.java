package com.thecrowstudios.meowmarket.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/account")
    public String account(Model model) {
        User user = userService.getUser();
        if (user == null) return "redirect:/api/auth/login";
        model.addAttribute("user", user);
        return "account";
    }
}
