package com.thecrowstudios.meowmarket.listings;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import com.thecrowstudios.meowmarket.authentication.User;
import com.thecrowstudios.meowmarket.cart.CartItem;
import com.thecrowstudios.meowmarket.orders.OrderListing;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Listing {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    private String title;
    
    @Column(length = 65555)
    private String description;
    
    @Column(length = 65555)
    private String longDescription;

    @Enumerated(EnumType.STRING)
    private ItemCategory category;

    private boolean featured;

    @Column(nullable=false, updatable = true)
    @ColumnDefault("3")
    private Double price;

    @Column(nullable=true, updatable = true)
    private Double originalPrice;

    private Integer quantityInStock;

    @Column(nullable = false, updatable = true)
    private String delivery;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime dateCreated;

    @Column(nullable = true, updatable = true)
    private LocalDateTime dateDeleted;

    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ListingImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CartItem> cartItems = new ArrayList<>();

    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<OrderListing> ordersListings = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "created_by_user_id", nullable = true)
    private User createdByUser;

    public enum ItemCategory {
        OBD_READERS, STEERING_WHEELS, DECALS, BOSS_KITS
    }

    public Listing() {
    }

    public Listing(Integer id, String title, String description, Double price, Integer quantityInStock) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.quantityInStock = quantityInStock;
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

    public boolean getFeatured() {
        return this.featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public Double getPrice() {
        return this.price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getOriginalPrice() {
        return this.originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
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

    public LocalDateTime getDateCreated() {
        return this.dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDateTime getDateDeleted() {
        return this.dateDeleted;
    }

    public void setDateDeleted(LocalDateTime dateDeleted) {
        this.dateDeleted = dateDeleted;
    }
    
    public Integer getId() {
        return id;
    }

    public List<ListingImage> getImages() {
        return this.images;
    }

    public void setImages(List<ListingImage> images) {
        this.images = images;
    }

    public List<CartItem> getCartItems() {
        return this.cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public Set<OrderListing> getOrdersListings() {
        return this.ordersListings;
    }

    public void setOrdersListings(Set<OrderListing> ordersListings) {
        this.ordersListings = ordersListings;
    }

    public User getCreatedByUser() {
        return this.createdByUser;
    }

    public void setCreatedByUser(User createdByUser) {
        this.createdByUser = createdByUser;
    }

    public void softDelete() {
        this.dateDeleted = LocalDateTime.now();
    }
}
