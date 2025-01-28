package com.thecrowstudios.meowmarket.cart;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import jakarta.transaction.Transactional;

public interface CartItemRepository extends CrudRepository<CartItem, Integer> {
    List<CartItem> findAllByUserId(Integer userId);

    List<CartItem> findAllBySessionId(String sessionId);

    @Transactional
    void deleteAllById_SessionId(String sessionId);
}
