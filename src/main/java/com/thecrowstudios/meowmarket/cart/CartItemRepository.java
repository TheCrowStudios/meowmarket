package com.thecrowstudios.meowmarket.cart;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface CartItemRepository extends CrudRepository<CartItem, Integer> {
    List<CartItem> findAllByUser_Id(Integer userId);

    List<CartItem> findAllBySession(String sessionId);

    Optional<CartItem> findByUser_IdAndListing_Id(Integer userId, Integer listingId);

    Optional<CartItem> findBySessionAndListing_Id(String session, Integer listingId);

    @Transactional
    void deleteAllBySession(String sessionId);

    @Transactional
    void removeByUser_IdAndListing_Id(Integer userId, Integer listingId);

    @Transactional
    void removeBySessionAndListing_Id(String session, Integer listingId);
}
