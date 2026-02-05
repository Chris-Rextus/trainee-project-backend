package com.catijr.peugeot.repository;

import com.catijr.peugeot.entities.TaskList;
import com.catijr.peugeot.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskListRepository extends JpaRepository<TaskList, UUID> {

    // Find all lists for a specific user
    List<TaskList> findByUser(User user);

    // Find all lists for a specific user ID
    List<TaskList> findByUserId(UUID userId);

    // Find a specific list by ID and user (for security/validation)
    Optional<TaskList> findByIdAndUserId(UUID id, UUID userId);

    // Find a specific list by ID and user object
    Optional<TaskList> findByIdAndUser(UUID id, User user);

    // Find max order value for a user's lists
    @Query("SELECT COALESCE(MAX(l.order), 0) FROM TaskList l WHERE l.user.id = :userId")
    Integer findMaxOrderByUserId(@Param("userId") UUID userId);

    // Find lists ordered by order field for a specific user
    List<TaskList> findByUserOrderByOrderAsc(User user);

    // Find lists by user ID ordered by order field
    List<TaskList> findByUserIdOrderByOrderAsc(UUID userId);

    // Check if list exists for user
    boolean existsByIdAndUserId(UUID id, UUID userId);

    // Count lists for a user
    long countByUserId(UUID userId);

    // Find list by name and user
    Optional<TaskList> findByNameAndUserId(String name, UUID userId);
}