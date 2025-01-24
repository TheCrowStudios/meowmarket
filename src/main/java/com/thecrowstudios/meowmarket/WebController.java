package com.thecrowstudios.meowmarket;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.thecrowstudios.meowmarket.cart.CartItem;
import com.thecrowstudios.meowmarket.cart.CartService;
import com.thecrowstudios.meowmarket.listings.Listing;
import com.thecrowstudios.meowmarket.listings.ListingRepository;

@Controller
public class WebController {
    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private CartService cartService;

    @GetMapping("/")
    public String home(Model model) {
        List<Listing> listings = new ArrayList<Listing>();
        List<Listing> newListings = new ArrayList<Listing>();
        listingRepository.findAllByDateCreated().forEach(listings::add);
        listingRepository.findAllByDateCreated().stream().limit(4).forEach(newListings::add);
        model.addAttribute("listings", listings);
        model.addAttribute("newListings", listings);

        return "index";
    }

    @GetMapping("/cart")
    public String cart(Model model) {
        List<CartItem> cartItems = cartService.getCartItems();
        model.addAttribute("cartItems", cartItems);

        return "cart";
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