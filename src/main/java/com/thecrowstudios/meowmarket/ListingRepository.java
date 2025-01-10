package com.thecrowstudios.meowmarket;

import org.springframework.data.repository.CrudRepository;

public interface ListingRepository extends CrudRepository<Listing, Integer> {
    
}
