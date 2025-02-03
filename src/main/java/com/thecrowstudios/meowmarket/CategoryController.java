package com.thecrowstudios.meowmarket;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thecrowstudios.meowmarket.listings.Listing;
import com.thecrowstudios.meowmarket.listings.ListingRepository;
import com.thecrowstudios.meowmarket.listings.Listing.ItemCategory;

@Controller
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private ListingRepository listingRepository;

    @GetMapping("/{category}")
    public String showCategory(@PathVariable String category, Model model) {
        List<Listing> listings = listingRepository.findByCategoryAndDateDeletedIsNull(ItemCategory.valueOf(category.toUpperCase()));
        model.addAttribute("listings", listings);
        model.addAttribute("category", category.toString().replace("_", " ").toUpperCase());
        return "category";
    }
}