package com.catijr.peugeot.repository;

import com.catijr.peugeot.entities.Priority;
import com.catijr.peugeot.entities.Task;
import com.catijr.peugeot.entities.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    // Find all tasks by a specific List (using object relationship)
    List<Task> findByList(TaskList list);

    // Find all tasks by a specific List ID (alternative)
    List<Task> findByListId(UUID listId);

    // Find tasks by a List and priority
    List<Task> findByListAndPriority(TaskList list, Priority priority);

    // Find tasks by List ID and priority (alternative)
    List<Task> findByListIdAndPriority(UUID listId, Priority priority);

    // Find completed or incompleted tasks for a list
    List<Task> findByListAndFinishedDateIsNotNull(TaskList list);
    List<Task> findByListAndFinishedDateIsNull(TaskList list);

    // Alternative by list ID
    List<Task> findByListIdAndFinishedDateIsNotNull(UUID listId);
    List<Task> findByListIdAndFinishedDateIsNull(UUID listId);

    // Find tasks that passed deadline
    List<Task> findByExpectedFinishDateBeforeAndFinishedDateIsNull(LocalDateTime date);

    // Find tasks by multiple lists (for current user) - updated to use relationship
    @Query("SELECT t FROM Task t WHERE t.list.id IN :listIds")
    List<Task> findByListIds(@Param("listIds") List<UUID> listIds);

    // Find max position in a list (for auto positioning) - updated
    @Query("SELECT COALESCE(MAX(t.position), 0) FROM Task t WHERE t.list.id = :listId")
    Integer findMaxPositionByListId(@Param("listId") UUID listId);

    // Alternative using object
    @Query("SELECT COALESCE(MAX(t.position), 0) FROM Task t WHERE t.list = :list")
    Integer findMaxPositionByList(@Param("list") TaskList list);

    // Count tasks per list
    long countByList(TaskList list);
    long countByListId(UUID listId);

    // Check if task exists in list - updated
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Task t WHERE t.id = :taskId AND t.list.id = :listId")
    boolean existsByIdAndListId(@Param("taskId") UUID taskId, @Param("listId") UUID listId);

    // Alternative using object
    boolean existsByIdAndList(UUID id, TaskList list);

    // Find tasks by user (through list relationship)
    @Query("SELECT t FROM Task t WHERE t.list.user.id = :userId")
    List<Task> findByUserId(@Param("userId") UUID userId);

    // Find tasks by user with priority filter
    @Query("SELECT t FROM Task t WHERE t.list.user.id = :userId AND t.priority = :priority")
    List<Task> findByUserIdAndPriority(@Param("userId") UUID userId, @Param("priority") Priority priority);

    // Find completed/incomplete tasks for a user
    @Query("SELECT t FROM Task t WHERE t.list.user.id = :userId AND t.finishedDate IS NOT NULL")
    List<Task> findCompletedTasksByUserId(@Param("userId") UUID userId);

    @Query("SELECT t FROM Task t WHERE t.list.user.id = :userId AND t.finishedDate IS NULL")
    List<Task> findIncompleteTasksByUserId(@Param("userId") UUID userId);

    // Find tasks ordered by position within a list
    List<Task> findByListOrderByPositionAsc(TaskList list);
    List<Task> findByListIdOrderByPositionAsc(UUID listId);
}