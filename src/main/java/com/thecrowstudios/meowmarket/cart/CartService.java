package com.thecrowstudios.meowmarket.cart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thecrowstudios.meowmarket.authentication.User;
import com.thecrowstudios.meowmarket.authentication.UserService;
import com.thecrowstudios.meowmarket.listings.Listing;
import com.thecrowstudios.meowmarket.listings.ListingRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class CartService {
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private UserService userService;

    /*
     * adds an item to cart based on session or user account
     */
    public void addOrUpdateCartItem(Integer listingId, Integer quantity) {
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new RuntimeException("No listing with id " + listingId));

        // TODO - use cookies instead of session
        if (userService.loggedIn()) {
            User user = userService.getUser();

            CartItem cartItem = new CartItem();
            cartItem.setListing(listing);
            cartItem.setUser(user);
            cartItem.setQuantity(quantity);

            cartItemRepository.save(cartItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setListing(listing);
            cartItem.setSession(httpSession.getId());
            
            cartItem.setQuantity(quantity);

            cartItemRepository.save(cartItem);
        }
    }

    public Integer getCartItemCount() {
        return getCartItems().size();
    }

    public List<CartItem> getCartItems() {
        if (userService.loggedIn())
            return cartItemRepository.findAllByUserId(userService.getUser().getId());

        return cartItemRepository.findAllBySessionId(httpSession.getId());
    }

    public void removeCartItem(Integer listingId) {
        // TODO - this
    }

    public void clearCart() {
        cartItemRepository.deleteAllById_SessionId(httpSession.getId());
    }

    public boolean isItemInCart(Integer listingId) {
        // TODO - this
        return false;
    }
}
