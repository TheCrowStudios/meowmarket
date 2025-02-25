package com.thecrowstudios.meowmarket.listings;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.thecrowstudios.meowmarket.listings.Listing.ItemCategory;

public class ListingDTO {
    private Integer id;
    private String title;
    private String description;
    private String longDescription;
    private ItemCategory category;
    private Boolean featured;
    private double price;
    private double originalPrice;
    private Integer quantityInStock;
    private String delivery;
    private List<MultipartFile> images;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Boolean isFeatured() {
        return this.featured;
    }

    public Boolean getFeatured() {
        return this.featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getOriginalPrice() {
        return this.originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public List<MultipartFile> getImages() {
        return this.images;
    }

    public Integer getQuantityInStock() {
        return this.quantityInStock;
    }

    public String getDelivery() {
        return this.delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public void setQuantityInStock(Integer quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public void setImages(List<MultipartFile> images) {
        this.images = images;
    }
}
