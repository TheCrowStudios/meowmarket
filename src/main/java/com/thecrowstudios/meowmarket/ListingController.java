package com.thecrowstudios.meowmarket;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/listings")
public class ListingController {
    @Autowired
    private ListingRepository listingRepository;

    @PostMapping(path="/addListing")
    public @ResponseBody String addNewListing (@RequestParam String title, @RequestParam String description, @RequestParam Integer quantityInStock) {
        Listing listing = new Listing();
        listing.setTitle(title);
        listing.setDescription(description);
        listing.setQuantityInStock(quantityInStock);
        listing.setDateCreated(LocalDateTime.now());
        listingRepository.save(listing);
        return "saved";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Listing> getAllListings() {
        return listingRepository.findAll();
    }
}
