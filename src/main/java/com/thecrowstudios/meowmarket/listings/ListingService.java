package com.thecrowstudios.meowmarket.listings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ListingService {
    @Autowired
    private ListingImageRepository listingImageRepository;

    @Value("${upload.path}")
    private String uploadPath;

    public Listing dtoToListing(ListingDTO listingDTO) throws IOException {
        Listing listing = new Listing();
        listing.setId(listingDTO.getId());
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

                if (!Files.exists(Path.of(uploadPath)))
                    Files.createDirectories(Path.of(uploadPath));
                Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                ListingImage listingImage = new ListingImage();
                listingImage.setImageUrl(filename);
                listingImage.setListing(listing);

                listing.getImages().add(listingImage);
            }
        }

        return listing;
    }

    public void clearListingImages(Integer listingId) throws IOException {
        List<ListingImage> listingImages = listingImageRepository.findAllByListingId(listingId);
        for (ListingImage listingImage : listingImages) {
            Files.deleteIfExists(Path.of(uploadPath, listingImage.getImageUrl()));
        }

        listingImageRepository.deleteAllByListingId(listingId); // clear all current images as this is just a very basic way of doing it
    }
}
