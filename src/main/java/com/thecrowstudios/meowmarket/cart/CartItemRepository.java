package com.thecrowstudios.meowmarket.cart;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import jakarta.transaction.Transactional;

public interface CartItemRepository extends CrudRepository<CartItem, Integer> {
    List<CartItem> findAllByUser_Id(Integer userId);

    List<CartItem> findAllBySession(String sessionId);

    @Transactional
    void deleteAllBySession(String sessionId);

    @Transactional
    void removeByUser_IdAndListing_Id(Integer userId, Integer listingId);

    @Transactional
    void removeBySessionAndListing_Id(String session, Integer listingId);
}
