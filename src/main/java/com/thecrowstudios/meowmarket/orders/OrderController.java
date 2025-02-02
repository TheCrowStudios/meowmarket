package com.thecrowstudios.meowmarket.orders;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.thecrowstudios.meowmarket.authentication.User;
import com.thecrowstudios.meowmarket.authentication.UserService;

@Controller
public class OrderController {
    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public String orders(Model model) {
        User user = userService.getUser();
        if (user == null) return "redirect:/api/auth/login";

        List<Order> orders = orderService.getOrders();

        model.addAttribute("orders", orders);
        return "orders";
    }
}
