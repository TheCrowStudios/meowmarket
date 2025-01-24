package com.thecrowstudios.meowmarket.cart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/add-to-cart")
    public ResponseEntity<String> addItemToCart(@RequestBody CartRequestDTO request) {
        cartService.addOrUpdateCartItem(request.getListingId(), request.getQuantity());
        return ResponseEntity.ok("Item added/updated in cart");
    }

    @GetMapping("/check/{listingId}")
    public ResponseEntity<Boolean> checkItemInCart(@PathVariable Integer listingId) {
        return ResponseEntity.ok(cartService.isItemInCart(listingId));
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCart() {
        return ResponseEntity.ok(cartService.getCartItems());
    }

    @DeleteMapping("/{listingId}")
    public ResponseEntity<String> removeItem(@PathVariable Integer listingId) {
        cartService.removeCartItem(listingId);
        return ResponseEntity.ok("Item removed from cart");
    }

    @DeleteMapping
    public ResponseEntity<String> clearCart() {
        cartService.clearCart();
        return ResponseEntity.ok("Cart cleared");
    }
}
