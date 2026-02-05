package com.catijr.peugeot.dto.task;

import com.catijr.peugeot.entities.Priority;
import com.catijr.peugeot.entities.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {

    private UUID id;
    private String name;
    private String description;
    private Priority priority;
    private LocalDateTime expectedFinishDate;
    private LocalDateTime finishedDate;
    private Integer position;
    private UUID listId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Computed fields
    private boolean completed;
    private boolean overdue;

    // Factory method: Entity to DTO
    public static TaskResponse fromEntity(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .priority(task.getPriority())
                .expectedFinishDate(task.getExpectedFinishDate())
                .finishedDate(task.getFinishedDate())
                .position(task.getPosition())
                .listId(task.getList() != null ? task.getList().getId() : null) // Fixed!
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .completed(task.isComplete())
                .overdue(isOverdue(task))
                .build();
    }

    // Helper to check if task is beyond the date
    private static boolean isOverdue(Task task) {
        if (task.isComplete() || task.getExpectedFinishDate() == null) {
            return false;
        }
        return task.getExpectedFinishDate().isBefore(LocalDateTime.now());
    }
}