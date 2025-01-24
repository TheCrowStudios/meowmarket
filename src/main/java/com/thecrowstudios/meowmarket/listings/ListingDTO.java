package com.thecrowstudios.meowmarket.listings;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.thecrowstudios.meowmarket.listings.Listing.ItemCategory;

public class ListingDTO {
    private String title;
    private String description;
    private String longDescription;
    private ItemCategory category;
    private double price;
    private Integer quantityInStock;
    private List<MultipartFile> images;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLongDescription() {
        return this.longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public ItemCategory getCategory() {
        return this.category;
    }

    public void setCategory(ItemCategory category) {
        this.category = category;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<MultipartFile> getImages() {
        return this.images;
    }

    public Integer getQuantityInStock() {
        return this.quantityInStock;
    }

    public void setQuantityInStock(Integer quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public void setImages(List<MultipartFile> images) {
        this.images = images;
    }
}
