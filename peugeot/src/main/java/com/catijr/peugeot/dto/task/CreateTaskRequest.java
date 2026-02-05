package com.catijr.peugeot.dto.task;

import com.catijr.peugeot.entities.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class CreateTaskRequest {

    @NotNull(message = "List ID is required")
    private UUID listId;

    @NotBlank(message = "Task name is required")
    @Size(max = 200, message = "Task name cannot exceed 200 characters")
    private String name;

    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    @NotNull(message = "Priority is required")
    private Priority priority;

    private LocalDateTime expectedFinishDate;

    @NotNull(message = "Position is required")
    private Integer position;
}