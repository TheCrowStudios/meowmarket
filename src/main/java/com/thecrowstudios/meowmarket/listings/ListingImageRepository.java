package com.thecrowstudios.meowmarket.listings;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import jakarta.transaction.Transactional;

public interface ListingImageRepository extends CrudRepository<ListingImage, Integer> {
    List<ListingImage> findAllByListingId(Integer listingId);

    @Transactional
    void deleteAllByListingId(Integer listingId);
}