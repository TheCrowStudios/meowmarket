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
        List<Listing> featuredListings = listingRepository.findByFeaturedTrueAndDateDeletedIsNull();
        listingRepository.findAllByDateCreatedAndDateDeletedIsNull().forEach(listings::add);
        listingRepository.findAllByDateCreatedAndDateDeletedIsNull().stream().limit(4).forEach(newListings::add);
        model.addAttribute("listings", listings);
        model.addAttribute("newListings", newListings);
        model.addAttribute("featuredListings", featuredListings);

        return "index";
    }

    @GetMapping("/cart")
    public String cart(Model model) {
        List<CartItem> cartItems = cartService.getCartItems();
        model.addAttribute("cartItems", cartItems);

        return "cart";
    }

    @GetMapping("/faq")
    public String faq() {
        return "faq";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("/checkout")
    public String checkout() {
        return "checkout";
    }

    @GetMapping("/return")
    public String returnPage() {
        return "return";
    }
}