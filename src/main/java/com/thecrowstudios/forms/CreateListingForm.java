package com.thecrowstudios.forms;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateListingForm {
    @NotNull
    private MultipartFile images;

    @NotNull
    @Size(min=8, max=80)
    private String title;

    @NotNull
    @Max(8000)
    private String description;

    @NotNull
    private float price;

    @NotNull
    private int quantityInStock;

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

    public float getPrice() {
        return this.price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantityInStock() {
        return this.quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public String toString() {
        return String.format("Listing(Title: %s, Description: %s, Price: %f, QuantityInStock: %d)", title, description, price, quantityInStock);
    }
}
