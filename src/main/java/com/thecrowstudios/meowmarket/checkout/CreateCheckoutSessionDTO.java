package com.thecrowstudios.meowmarket.checkout;

public class CreateCheckoutSessionDTO {
    private Integer listingId;
    private Integer quantity;

    public Integer getListingId() {
        return this.listingId;
    }

    public void setListingId(Integer listingId) {
        this.listingId = listingId;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
