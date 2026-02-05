package com.catijr.peugeot.repository;

import com.catijr.peugeot.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRespository extends JpaRepository<User, UUID> {

    // find by email (case-sensitive)
    Optional<User> findByEmail(String email);

    // find by username (case-sensitive)
    Optional<User> findByUsername(String username);

    // checks email existence
    boolean existsByEmail(String email);

    // checks username existence
    boolean existsByUsername(String username);

}