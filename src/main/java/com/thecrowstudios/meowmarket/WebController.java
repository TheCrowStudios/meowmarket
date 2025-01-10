package com.thecrowstudios.meowmarket;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {
    @GetMapping("/item")
    public String mapping(@RequestParam(name="id", required=true) int id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute(null, model);
        return "listing";
    }

    @GetMapping("/createListing")
    public String createListing() {
        return "createListing";
    }
}
    