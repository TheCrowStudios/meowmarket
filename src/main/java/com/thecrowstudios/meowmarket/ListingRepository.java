package com.thecrowstudios.meowmarket;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ListingRepository extends CrudRepository<Listing, Integer> {
    @Query("SELECT listing FROM Listing listing LEFT JOIN FETCH listing.images image WHERE listing.id = :id")
    Optional<Listing> findByIdWithImages(@Param("id") Integer id);
}
