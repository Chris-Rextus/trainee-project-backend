package com.catijr.peugeot.controller;

import com.catijr.peugeot.dto.tasklist.TaskListRequestDTO;
import com.catijr.peugeot.dto.tasklist.TaskListResponseDTO;
import com.catijr.peugeot.services.TaskListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/lists")
@RequiredArgsConstructor
public class TaskListController {

    private final TaskListService taskListService;

    // POST /lists
    @PostMapping
    public ResponseEntity<TaskListResponseDTO> createTaskList(
            @Valid @RequestBody TaskListRequestDTO request,
            @AuthenticationPrincipal UUID userId) {

        TaskListResponseDTO createdList = taskListService.createTaskList(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdList);
    }

    // GET /lists
    @GetMapping
    public ResponseEntity<List<TaskListResponseDTO>> getAllTaskLists(
            @AuthenticationPrincipal UUID userId) {

        List<TaskListResponseDTO> lists = taskListService.getAllTaskLists(userId);
        return ResponseEntity.ok(lists);
    }

    // GET /lists/{id}
    @GetMapping("/{id}")
    public ResponseEntity<TaskListResponseDTO> getTaskListById(
            @PathVariable UUID id,
            @AuthenticationPrincipal UUID userId) {

        TaskListResponseDTO list = taskListService.getTaskListById(id, userId);
        return ResponseEntity.ok(list);
    }

    // PUT /lists/{id}
    @PutMapping("/{id}")
    public ResponseEntity<TaskListResponseDTO> updateTaskList(
            @PathVariable UUID id,
            @Valid @RequestBody TaskListRequestDTO request,
            @AuthenticationPrincipal UUID userId) {

        TaskListResponseDTO updatedList = taskListService.updateTaskList(id, request, userId);
        return ResponseEntity.ok(updatedList);
    }

    // DELETE /lists/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaskList(
            @PathVariable UUID id,
            @AuthenticationPrincipal UUID userId) {

        taskListService.deleteTaskList(id, userId);
        return ResponseEntity.noContent().build();
    }

    // PATCH /lists/{id}/reorder
    @PatchMapping("/{id}/reorder")
    public ResponseEntity<TaskListResponseDTO> reorderTaskList(
            @PathVariable UUID id,
            @RequestParam Integer newOrder,
            @AuthenticationPrincipal UUID userId) {

        TaskListResponseDTO updatedList = taskListService.reorderTaskList(id, newOrder, userId);
        return ResponseEntity.ok(updatedList);
    }
}