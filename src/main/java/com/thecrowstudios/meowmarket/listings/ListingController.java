package com.thecrowstudios.meowmarket.listings;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.thecrowstudios.meowmarket.authentication.User;
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
        Listing listing = listingRepository.findByIdWithImagesAndDateDeletedIsNull(Integer.parseInt(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("listing", listing);
        return "listing";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String getEditListing(@PathVariable String id, Model model) {
        Listing listing = listingRepository.findById(Integer.parseInt(id))
                .orElse(null);

        if (listing == null) {
            return "redirect:/";
        }

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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("listingDTO", new ListingDTO());
        model.addAttribute("endpoint", "create");
        return "create-listing";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/create")
    public String addNewListing(@ModelAttribute ListingDTO listingDTO, Model model) throws IOException {
        User user = userService.getUser();

        if (user == null) {
            model.addAttribute("listingDTO", new ListingDTO());
            model.addAttribute("endpoint", "create");
            return "create-listing";
        }

        Listing listing = listingService.dtoToListing(listingDTO);
        listing.setCreatedByUser(user);

        listingRepository.save(listing);
        return "redirect:/";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/edit")
    public String editListing(@ModelAttribute ListingDTO listingDTO, Model model) throws IOException {
        System.out.println("post to edit listing");

        Listing listing = listingRepository.findById(listingDTO.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        System.out.println("size of listing images: " + listingDTO.getImages().size());
        if (listingDTO.getImages().size() > 1) // set to 1 by default for some reason?
            listingService.clearListingImages(listingDTO.getId());
        Listing listingEdited = listingService.dtoToListing(listingDTO);

        listingEdited.setCreatedByUser(listing.getCreatedByUser());
        if (listingDTO.getImages().size() == 1)
            listingEdited.setImages(listing.getImages());

        listingRepository.save(listingEdited);
        return "redirect:/listings/" + listing.getId();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/remove/{listingId}")
    public String removeListing(@PathVariable Integer listingId) {
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        listing.softDelete();
        listingRepository.save(listing);
        return "redirect:/";
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
