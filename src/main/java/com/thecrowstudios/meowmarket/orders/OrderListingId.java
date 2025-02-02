package com.thecrowstudios.meowmarket.orders;

import java.io.Serializable;
import java.util.Objects;

import com.thecrowstudios.meowmarket.listings.Listing;

public class OrderListingId implements Serializable {
    private Order order;
    private Listing listing;

    public OrderListingId() {
    }

    public OrderListingId(Order order, Listing listing) {
        this.order = order;
        this.listing = listing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderListingId that = (OrderListingId) o;

        if (order != null ? !order.equals(that.order) : that.order != null) return false;
        return listing != null ? listing.equals(that.listing) : that.listing == null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, listing);
    }
}
