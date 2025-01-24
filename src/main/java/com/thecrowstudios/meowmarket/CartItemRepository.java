package com.thecrowstudios.meowmarket;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import jakarta.transaction.Transactional;

public interface CartItemRepository extends CrudRepository<CartItem, CartItemId> {
    List<CartItem> findById_SessionId(String sessionId);

    @Transactional
    void deleteAllById_SessionId(String sessionId);

    boolean existsById(CartItemId id);
}
