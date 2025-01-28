package com.thecrowstudios.meowmarket.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrdersController {
    @Autowired
    UserService userService;

    @GetMapping("/orders")
    public String orders(Model model) {
        if (!userService.loggedIn()) return "redirect:/login";
        return "orders";
    }
}
