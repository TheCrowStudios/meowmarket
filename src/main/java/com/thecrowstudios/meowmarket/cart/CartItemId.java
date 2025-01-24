package com.thecrowstudios.meowmarket.cart;

import java.io.Serializable;

import com.thecrowstudios.meowmarket.listings.Listing;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Embeddable
public class CartItemId implements Serializable {
    private String sessionId;

    @ManyToOne
    @JoinColumn(name = "listing_id", insertable = false, updatable = false)
    private Listing listing;

    public CartItemId() {
    }

    public CartItemId(String sessionId, Listing listing) {
        this.sessionId = sessionId;
        this.listing = listing;
    }

    public CartItemId(String sessionId, Integer listingId) {
        this.sessionId = sessionId;
        this.listing = new Listing();
        this.listing.setId(listingId);
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Listing getListing() {
        return this.listing;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }
}