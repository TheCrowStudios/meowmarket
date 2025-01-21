package com.thecrowstudios.meowmarket;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class ListingDTO {
    private String title;
    private String description;
    private String longDescription;
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
