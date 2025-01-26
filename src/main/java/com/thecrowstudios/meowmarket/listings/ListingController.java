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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.thecrowstudios.meowmarket.authentication.UserService;

@Controller
@RequestMapping("/listings")
public class ListingController {
    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private UserService userService;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/{id}")
    public String listing (@PathVariable String id, Model model) {
        // TODO - retrieve listing from database
        Listing listing = listingRepository.findByIdWithImages(Integer.parseInt(id)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("listing", listing);
        return "listing";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("listingDTO", new ListingDTO());
        if (userService.isAdmin()) return "create-listing";
        return "redirect:/";
    }

    @PostMapping(path="/create")
    public String addNewListing (@ModelAttribute ListingDTO listingDTO) throws IOException {
        if (!userService.isAdmin()) return "redirect:/";
        
        Listing listing = new Listing();
        listing.setTitle(listingDTO.getTitle());
        listing.setDescription(listingDTO.getDescription());
        listing.setLongDescription(listingDTO.getLongDescription());
        listing.setCategory(listingDTO.getCategory());
        listing.setPrice(listingDTO.getPrice());
        listing.setQuantityInStock(listingDTO.getQuantityInStock());

        List<MultipartFile> images = listingDTO.getImages();
        
        for (int i = 0; i < images.size(); i++) {
            MultipartFile image = images.get(i);

            if (!image.isEmpty()) {
                String filename = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
                Path filePath = Path.of(uploadPath, filename);

                if (!Files.exists(Path.of(uploadPath))) Files.createDirectories(Path.of(uploadPath));
                Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                ListingImage listingImage = new ListingImage();
                listingImage.setImageUrl(filename);
                listingImage.setListing(listing);

                listing.getImages().add(listingImage);
            }
        }

        listingRepository.save(listing);
        return "redirect:/";
    }

    // @PostMapping(path="/create")
    // public @ResponseBody String addNewListing (@RequestParam String title, @RequestParam String description, @RequestParam Integer quantityInStock) {
    //     Listing listing = new Listing();
    //     listing.setTitle(title);
    //     listing.setDescription(description);
    //     listing.setQuantityInStock(quantityInStock);
    //     listing.setDateCreated(LocalDateTime.now());
    //     listingRepository.save(listing);
    //     return "saved";
    // }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Listing> getAllListings() {
        return listingRepository.findAll();
    }
}
