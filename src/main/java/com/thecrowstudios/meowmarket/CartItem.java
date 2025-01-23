package com.thecrowstudios.meowmarket;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class CartItem {
    @EmbeddedId
    private CartItemId id;

    private Integer quantity;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = true, updatable = false)
    @ManyToOne
    private User user;

    public CartItem() {
    }

    public CartItem(String sessionId, Listing listing, Integer quantity) {
        this.id = new CartItemId(sessionId, listing.getId());
        this.quantity = quantity;
    }

    public CartItem(String sessionId, Integer listingId, Integer quantity) {
        this.id = new CartItemId(sessionId, listingId);
        this.quantity = quantity;
    }

    public CartItemId getId() {
        return this.id;
    }

    public void setId(CartItemId id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}