package com.thecrowstudios.meowmarket.authentication;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    UserDetails findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndPassword(String email, String password);

    // @Transactional
    // @Modifying
    // @Query("UPDATE User usr SET usr.session = :session WHERE usr.id = :id")
    // void login(@Param("session") String session, @Param("id") Integer id);

    // @Transactional
    // @Modifying
    // @Query("UPDATE User usr SET usr.session = null WHERE usr.session = :session")
    // void clearSession(@Param("session") String session);
}
