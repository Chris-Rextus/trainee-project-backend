package com.catijr.peugeot.controller;

import com.catijr.peugeot.dto.task.CreateTaskRequest;
import com.catijr.peugeot.dto.task.TaskResponse;
import com.catijr.peugeot.dto.task.UpdateTaskRequest;
import com.catijr.peugeot.services.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    // POST /tasks
    @PostMapping("/tasks")
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody CreateTaskRequest request,
            @AuthenticationPrincipal UUID userId) {

        TaskResponse createdTask = taskService.createTask(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    // GET /tasks
    @GetMapping("/tasks")
    public ResponseEntity<List<TaskResponse>> getAllTasks(
            @AuthenticationPrincipal UUID userId) {

        List<TaskResponse> tasks = taskService.getAllTasks(userId);
        return ResponseEntity.ok(tasks);
    }

    // GET /lists/{id}/tasks
    @GetMapping("/lists/{listId}/tasks")
    public ResponseEntity<List<TaskResponse>> getTasksByListId(
            @PathVariable UUID listId,
            @AuthenticationPrincipal UUID userId) {

        List<TaskResponse> tasks = taskService.getTasksByListId(listId, userId);
        return ResponseEntity.ok(tasks);
    }

    // GET /tasks/{id}
    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskResponse> getTaskById(
            @PathVariable UUID id,
            @AuthenticationPrincipal UUID userId) {

        TaskResponse task = taskService.getTaskById(id, userId);
        return ResponseEntity.ok(task);
    }

    // PUT /tasks/{id}
    @PutMapping("/tasks/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTaskRequest request,
            @AuthenticationPrincipal UUID userId) {

        TaskResponse updatedTask = taskService.updateTask(id, request, userId);
        return ResponseEntity.ok(updatedTask);
    }

    // DELETE /tasks/{id}
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable UUID id,
            @AuthenticationPrincipal UUID userId) {

        taskService.deleteTask(id, userId);
        return ResponseEntity.noContent().build();
    }

    // PATCH /tasks/{id}/complete
    @PatchMapping("/tasks/{id}/complete")
    public ResponseEntity<TaskResponse> markTaskAsComplete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UUID userId) {

        TaskResponse updatedTask = taskService.markTaskAsComplete(id, userId);
        return ResponseEntity.ok(updatedTask);
    }

    // PATCH /tasks/{id}/incomplete
    @PatchMapping("/tasks/{id}/incomplete")
    public ResponseEntity<TaskResponse> markTaskAsIncomplete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UUID userId) {

        TaskResponse updatedTask = taskService.markTaskAsIncomplete(id, userId);
        return ResponseEntity.ok(updatedTask);
    }

    // PATCH /tasks/{id}/reorder
    @PatchMapping("/tasks/{id}/reorder")
    public ResponseEntity<TaskResponse> reorderTask(
            @PathVariable UUID id,
            @RequestParam Integer position,
            @AuthenticationPrincipal UUID userId) {

        TaskResponse updatedTask = taskService.reorderTask(id, position, userId);
        return ResponseEntity.ok(updatedTask);
    }
}