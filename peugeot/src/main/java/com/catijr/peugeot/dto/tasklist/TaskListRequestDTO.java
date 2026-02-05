package com.catijr.peugeot.dto.tasklist;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskListRequestDTO {

    @NotBlank(message = "List name is required")
    @Size(min = 1, max = 200, message = "List name must be between 1 and 200 characters")
    private String name;

    @NotNull(message = "Order is required")
    private Integer order;

}
