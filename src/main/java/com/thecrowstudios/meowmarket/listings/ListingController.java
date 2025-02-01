package com.thecrowstudios.meowmarket.listings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.thecrowstudios.meowmarket.authentication.UserService;

@Controller
@RequestMapping("/listings")
public class ListingController {
    @Autowired
    private ListingService listingService;

    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public String listing(@PathVariable String id, Model model) {
        // TODO - retrieve listing from database
        Listing listing = listingRepository.findByIdWithImages(Integer.parseInt(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("listing", listing);
        return "listing";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String getEditListing (@PathVariable String id, Model model) {
        Listing listing = listingRepository.findById(Integer.parseInt(id)).orElseThrow(() -> new RuntimeException("No listing with such id"));
        ListingDTO listingDTO = new ListingDTO();
        listingDTO.setId(listing.getId());
        listingDTO.setTitle(listing.getTitle());
        listingDTO.setCategory(listing.getCategory());
        listingDTO.setDescription(listing.getDescription());
        listingDTO.setPrice(listing.getPrice());
        listingDTO.setQuantityInStock(listing.getQuantityInStock());
        listingDTO.setDelivery(listing.getDelivery());
        listingDTO.setLongDescription(listing.getLongDescription());
        model.addAttribute("listingDTO", listingDTO);
        model.addAttribute("endpoint", "edit");
        return "create-listing";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {

        model.addAttribute("listingDTO", new ListingDTO());
        model.addAttribute("endpoint", "create");
        return "create-listing";
    }

    @PostMapping(path = "/create")
    public String addNewListing(@ModelAttribute ListingDTO listingDTO) throws IOException {

        Listing listing = listingService.dtoToListing(listingDTO);

        listingRepository.save(listing);
        return "redirect:/";
    }

    @PostMapping("/edit")
    public String editListing(@ModelAttribute ListingDTO listingDTO, Model model) throws IOException {

        listingService.clearListingImages(listingDTO.getId());
        Listing listing = listingService.dtoToListing(listingDTO);

        listingRepository.save(listing);
        return "redirect:/listings/" + listing.getId();
    }

    // @PostMapping(path="/create")
    // public @ResponseBody String addNewListing (@RequestParam String title,
    // @RequestParam String description, @RequestParam Integer quantityInStock) {
    // Listing listing = new Listing();
    // listing.setTitle(title);
    // listing.setDescription(description);
    // listing.setQuantityInStock(quantityInStock);
    // listing.setDateCreated(LocalDateTime.now());
    // listingRepository.save(listing);
    // return "saved";
    // }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<Listing> getAllListings() {
        return listingRepository.findAll();
    }
}
