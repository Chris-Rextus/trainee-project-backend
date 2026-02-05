package com.catijr.peugeot.services;

import com.catijr.peugeot.dto.task.CreateTaskRequest;
import com.catijr.peugeot.dto.task.TaskResponse;
import com.catijr.peugeot.dto.task.UpdateTaskRequest;
import com.catijr.peugeot.entities.Task;
import com.catijr.peugeot.entities.TaskList;
import com.catijr.peugeot.exceptions.ResourceNotFoundException;
import com.catijr.peugeot.repository.TaskListRepository;
import com.catijr.peugeot.repository.TaskRepository;
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
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskListRepository taskListRepository;

    // POST /tasks - Create a new task
    public TaskResponse createTask(CreateTaskRequest request, UUID userId) {
        log.info("Creating task for user: {}", userId);

        // Find the list (with user validation)
        TaskList list = taskListRepository.findByIdAndUserId(request.getListId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Task list not found with id: " + request.getListId() + " for user: " + userId));

        // Check if position is provided, otherwise assign to the end
        Integer position = request.getPosition();
        if (position == null) {
            position = taskRepository.findMaxPositionByListId(list.getId()) + 1;
        }

        // Create task entity
        Task task = Task.builder()
                .name(request.getName())
                .description(request.getDescription())
                .priority(request.getPriority())
                .expectedFinishDate(request.getExpectedFinishDate())
                .position(position)
                .list(list)
                .build();

        // Use entity's business logic to maintain bidirectional relationship
        list.addTask(task);

        // save
        taskListRepository.save(list);

        // Return created task
        Task savedTask = taskRepository.findById(task.getId())
                .orElseThrow(() -> new RuntimeException("Task not saved properly"));

        log.info("Created task with id: {}", savedTask.getId());
        return TaskResponse.fromEntity(savedTask);
    }

    // GET /tasks - Get all tasks for a user
    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks(UUID userId) {
        log.info("Getting all tasks for user: {}", userId);

        List<Task> tasks = taskRepository.findByUserId(userId);
        return tasks.stream()
                .map(TaskResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // GET /lists/{id}/tasks - Get all tasks for a specific list
    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksByListId(UUID listId, UUID userId) {
        log.info("Getting tasks for list: {} for user: {}", listId, userId);

        // Verify list belongs to user
        if (!taskListRepository.existsByIdAndUserId(listId, userId)) {
            throw new ResourceNotFoundException(
                    "Task list not found with id: " + listId + " for user: " + userId);
        }

        List<Task> tasks = taskRepository.findByListIdOrderByPositionAsc(listId);
        return tasks.stream()
                .map(TaskResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // GET /tasks/{id} - Get a specific task by ID
    @Transactional(readOnly = true)
    public TaskResponse getTaskById(UUID taskId, UUID userId) {
        log.info("Getting task {} for user: {}", taskId, userId);

        Task task = findTaskWithUserValidation(taskId, userId);
        return TaskResponse.fromEntity(task);
    }

    // PUT /tasks/{id} - Update a task
    public TaskResponse updateTask(UUID taskId, UpdateTaskRequest request, UUID userId) {
        log.info("Updating task {} for user: {}", taskId, userId);

        Task task = findTaskWithUserValidation(taskId, userId);

        // Update fields if provided
        if (request.getName() != null) {
            task.setName(request.getName());
        }

        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }

        if (request.getPriority() != null) {
            task.setPriority(request.getPriority());
        }

        if (request.getExpectedFinishDate() != null) {
            task.setExpectedFinishDate(request.getExpectedFinishDate());
        }

        if (request.getPosition() != null) {
            task.setPosition(request.getPosition());
        }

        // Handle completion status
        if (request.getCompleted() != null) {
            if (request.getCompleted() && !task.isComplete()) {
                task.markAsCompleted();
            } else if (!request.getCompleted() && task.isComplete()) {
                task.markAsIncomplete();
            }
        }

        // Handle finishedDate directly if provided
        if (request.getFinishedDate() != null) {
            task.setFinishedDate(request.getFinishedDate());
        }

        Task updatedTask = taskRepository.save(task);
        log.info("Updated task: {}", taskId);

        return TaskResponse.fromEntity(updatedTask);
    }

    // DELETE /tasks/{id} - Delete a task
    public void deleteTask(UUID taskId, UUID userId) {
        log.info("Deleting task {} for user: {}", taskId, userId);

        Task task = findTaskWithUserValidation(taskId, userId);

        // Remove task from its list
        TaskList list = task.getList();
        list.removeTask(task);

        // Save the list
        taskListRepository.save(list);
        log.info("Deleted task: {}", taskId);
    }

    // PATCH /tasks/{id}/complete - Mark task as complete
    public TaskResponse markTaskAsComplete(UUID taskId, UUID userId) {
        log.info("Marking task {} as complete for user: {}", taskId, userId);

        Task task = findTaskWithUserValidation(taskId, userId);
        task.markAsCompleted();

        Task updatedTask = taskRepository.save(task);
        return TaskResponse.fromEntity(updatedTask);
    }

    // PATCH /tasks/{id}/incomplete - Mark task as incomplete
    public TaskResponse markTaskAsIncomplete(UUID taskId, UUID userId) {
        log.info("Marking task {} as incomplete for user: {}", taskId, userId);

        Task task = findTaskWithUserValidation(taskId, userId);
        task.markAsIncomplete();

        Task updatedTask = taskRepository.save(task);
        return TaskResponse.fromEntity(updatedTask);
    }

    // PATCH /tasks/{id}/reorder - Reorder task within its list
    public TaskResponse reorderTask(UUID taskId, Integer newPosition, UUID userId) {
        log.info("Reordering task {} to position {} for user: {}", taskId, newPosition, userId);

        Task task = findTaskWithUserValidation(taskId, userId);
        task.setPosition(newPosition);

        Task updatedTask = taskRepository.save(task);
        return TaskResponse.fromEntity(updatedTask);
    }

    // Helper method to find task with user validation
    private Task findTaskWithUserValidation(UUID taskId, UUID userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        // Check if task belongs to user
        if (!task.getList().getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException(
                    "Task not found with id: " + taskId + " for user: " + userId);
        }

        return task;
    }
}