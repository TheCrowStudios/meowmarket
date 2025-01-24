package com.thecrowstudios.meowmarket.cart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
public class CartService {
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private HttpSession httpSession;

    public void addOrUpdateCartItem(Integer listingId, Integer quantity) {
        String sessionId = httpSession.getId();
        CartItemId cartItemId = new CartItemId(sessionId, listingId);

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElse(new CartItem(sessionId, listingId, quantity));
        
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
    }

    public Integer getCartItemCount() {
        return getCartItems().size();
    }

    public List<CartItem> getCartItems() {
        return cartItemRepository.findById_SessionId(httpSession.getId());
    }

    public void removeCartItem(Integer listingId) {
        CartItemId cartItemId = new CartItemId(httpSession.getId(), listingId);
        cartItemRepository.deleteById(cartItemId);
    }

    public void clearCart() {
        cartItemRepository.deleteAllById_SessionId(httpSession.getId());
    }

    public boolean isItemInCart(Integer listingId) {
        CartItemId cartItemId = new CartItemId(httpSession.getId(), listingId);
        return cartItemRepository.existsById(cartItemId);
    }
}
