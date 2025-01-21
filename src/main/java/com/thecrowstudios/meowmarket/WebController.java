package com.thecrowstudios.meowmarket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.thecrowstudios.forms.CreateListingForm;

import jakarta.validation.Valid;

@Controller
public class WebController {
    @Autowired
    private ListingRepository listingRepository;

    @GetMapping("/")
    public String home(Model model) {
        List<Listing> listings = new ArrayList<Listing>();
        listingRepository.findAll().forEach(listings::add);
        model.addAttribute("listings", listings);
        model.addAttribute("itemsInCart", 1);

        return "index";
    }

    // @GetMapping("/createListing")
    // public String showCreateListing() {
    // return "createListing";
    // }

    // @PostMapping("/createListing")
    // public String createListing(@Valid CreateListingForm createListingForm,
    // BindingResult bindingResult) {
    // if (bindingResult.hasErrors()) {
    // return "createListing";
    // }

    // int listingId = 0;

    // return "redirect:/listings/" + listingId;
    // }
}