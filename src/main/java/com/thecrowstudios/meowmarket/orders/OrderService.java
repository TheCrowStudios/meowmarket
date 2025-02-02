package com.thecrowstudios.meowmarket.orders;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thecrowstudios.meowmarket.authentication.User;
import com.thecrowstudios.meowmarket.authentication.UserService;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired UserService userService;

    public List<Order> getOrders() {
        User user = userService.getUser();

        if (user != null) {
            return orderRepository.findAllByUser_Id(user.getId());
        }

        return new ArrayList<Order>();
    }
}
