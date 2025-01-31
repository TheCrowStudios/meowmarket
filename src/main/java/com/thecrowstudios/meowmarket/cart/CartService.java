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

        User user = userService.getUser();
        CartItem cartItem = null;

        if (user != null) {
            if (cartItem == null) cartItem = cartItemRepository.findByUser_IdAndListing_Id(user.getId(), listingId).orElse(new CartItem());
            cartItem.setListing(listing);
            cartItem.setUser(user);
            cartItem.setQuantity(quantity);

            cartItemRepository.save(cartItem);
        } else {
            if (cartItem == null) cartItem = cartItemRepository.findBySessionAndListing_Id(httpSession.getId(), listingId).orElse(new CartItem());
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
        User user = userService.getUser();
        if (user != null)
            return cartItemRepository.findAllByUser_Id(userService.getUser().getId());

        return cartItemRepository.findAllBySession(httpSession.getId());
    }

    public void removeCartItem(Integer listingId) {
        User user = userService.getUser();
        if (user != null)
            cartItemRepository.removeByUser_IdAndListing_Id(user.getId(), listingId);

        cartItemRepository.removeBySessionAndListing_Id(httpSession.getId(), listingId);
    }

    public void clearCart() {
        cartItemRepository.deleteAllBySession(httpSession.getId());
    }

    public boolean isItemInCart(Integer listingId) {
        // TODO - this
        return false;
    }
}
