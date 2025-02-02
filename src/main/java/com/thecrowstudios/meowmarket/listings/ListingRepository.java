package com.thecrowstudios.meowmarket.listings;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ListingRepository extends CrudRepository<Listing, Integer> {
    @Query("SELECT listing FROM Listing listing LEFT JOIN FETCH listing.images image WHERE listing.id = :id")
    Optional<Listing> findByIdWithImages(@Param("id") Integer id);

    @Query("SELECT listing FROM Listing listing LEFT JOIN FETCH listing.images image WHERE listing.dateDeleted IS NULL ORDER BY listing.dateCreated DESC")
    List<Listing> findAllByDateCreated();

    List<Listing> findByCategory(Listing.ItemCategory category);
}
