package com.thecrowstudios.meowmarket;

import jakarta.persistence.Entity;

@Entity
public class CartListing {
    private Integer sessionId;

    private Integer listingId;

    private Integer quantity;
}
