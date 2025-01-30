package com.thecrowstudios.meowmarket.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrdersController {
    @Autowired
    private UserService userService;

    @GetMapping("/orders")
    public String orders(Model model) {
        User user = userService.getUser();
        if (user == null) return "redirect:/api/auth/login";
        return "orders";
    }
}
