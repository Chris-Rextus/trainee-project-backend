package com.catijr.peugeot.services;

import com.catijr.peugeot.dto.tasklist.TaskListRequestDTO;
import com.catijr.peugeot.dto.tasklist.TaskListResponseDTO;
import com.catijr.peugeot.entities.TaskList;
import com.catijr.peugeot.entities.User;
import com.catijr.peugeot.exceptions.ResourceNotFoundException;
import com.catijr.peugeot.repository.TaskListRepository;
import com.catijr.peugeot.repository.UserRespository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TaskListService {

    private final TaskListRepository taskListRepository;
    private final UserRespository userRepository;

    // POST /lists - Create a new task list
    public TaskListResponseDTO createTaskList(TaskListRequestDTO request, UUID userId) {

        log.info("Creating task list for user: {}", userId);

        // Find user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Check if order is provided, otherwise assign to the end
        Integer order = request.getOrder();
        if (order == null) {
            order = taskListRepository.findMaxOrderByUserId(userId) + 1;
        }

        // Create task list entity
        TaskList taskList = TaskList.builder()
                .name(request.getName())
                .order(order)
                .user(user)
                .build();

        // Save to database
        TaskList savedTaskList = taskListRepository.save(taskList);
        log.info("Created task list with id: {}", savedTaskList.getId());

        return convertToResponse(savedTaskList);
    }

    // GET /lists - Get all task lists for a user
    @Transactional(readOnly = true)
    public List<TaskListResponseDTO> getAllTaskLists(UUID userId) {
        log.info("Getting all task lists for user: {}", userId);

        // Verify user exists
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        // Get lists ordered by order field
        List<TaskList> taskLists = taskListRepository.findByUserIdOrderByOrderAsc(userId);

        return taskLists.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // GET /lists/{id} - Get a specific task list by ID
    @Transactional(readOnly = true)
    public TaskListResponseDTO getTaskListById(UUID listId, UUID userId) {
        log.info("Getting task list {} for user: {}", listId, userId);

        // Find task list with user validation
        TaskList taskList = taskListRepository.findByIdAndUserId(listId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Task list not found with id: " + listId + " for user: " + userId));

        return convertToResponse(taskList);
    }

    // PUT /lists/{id} - Update a task list
    public TaskListResponseDTO updateTaskList(UUID listId, TaskListRequestDTO request, UUID userId) {
        log.info("Updating task list {} for user: {}", listId, userId);

        // Find existing task list with user validation
        TaskList taskList = taskListRepository.findByIdAndUserId(listId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Task list not found with id: " + listId + " for user: " + userId));

        // Update fields
        taskList.setName(request.getName());
        taskList.setOrder(request.getOrder());

        // Save updates
        TaskList updatedTaskList = taskListRepository.save(taskList);
        log.info("Updated task list: {}", listId);

        return convertToResponse(updatedTaskList);
    }

    // DELETE /lists/{id} - Delete a task list
    public void deleteTaskList(UUID listId, UUID userId) {
        log.info("Deleting task list {} for user: {}", listId, userId);

        // Verify list exists and belongs to user
        if (!taskListRepository.existsByIdAndUserId(listId, userId)) {
            throw new ResourceNotFoundException(
                    "Task list not found with id: " + listId + " for user: " + userId);
        }

        // Delete the list (cascade will delete tasks due to orphanRemoval = true)
        taskListRepository.deleteById(listId);
        log.info("Deleted task list: {}", listId);
    }

    // PATCH /lists/{id}/reorder - Reorder a task list
    public TaskListResponseDTO reorderTaskList(UUID listId, Integer newOrder, UUID userId) {
        log.info("Reordering task list {} to position {} for user: {}", listId, newOrder, userId);

        // Find the task list
        TaskList taskList = taskListRepository.findByIdAndUserId(listId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Task list not found with id: " + listId + " for user: " + userId));

        // Update order
        taskList.setOrder(newOrder);

        // Save the change
        TaskList updatedTaskList = taskListRepository.save(taskList);

        return convertToResponse(updatedTaskList);
    }

    // Helper method to convert entity to DTO response
    private TaskListResponseDTO convertToResponse(TaskList taskList) {
        return TaskListResponseDTO.builder()
                .id(taskList.getId())
                .name(taskList.getName())
                .order(taskList.getOrder())
                .createdAt(taskList.getCreatedAt())
                .updatedAt(taskList.getUpdatedAt())
                .taskIds(taskList.getTasks().stream()
                        .map(task -> task.getId())
                        .collect(Collectors.toList()))
                .build();
    }
}