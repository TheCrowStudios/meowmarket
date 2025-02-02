package com.thecrowstudios.meowmarket.orders;

import com.thecrowstudios.meowmarket.listings.Listing;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_listing")
@IdClass(OrderListingId.class)
public class OrderListing {
    @Id
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Id
    @ManyToOne
    @JoinColumn(name = "listing_id", nullable = false)
    private Listing listing;

    private Integer quantity;

    private Double amountPaid;

    public OrderListing() {
    }

    public OrderListing(Order order, Listing listing, Integer quantity, Double amountPaid) {
        this.order = order;
        this.listing = listing;
        this.quantity = quantity;
        this.amountPaid = amountPaid;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Listing getListing() {
        return this.listing;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getAmountPaid() {
        return this.amountPaid;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }

}
