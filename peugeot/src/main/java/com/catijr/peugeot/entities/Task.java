package com.catijr.peugeot.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Task {

    @Id
    @   GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(nullable = false, length = 200)
    @NotBlank(message = "Task name is required.")
    //@Size(max = 200, message = "Task name cannot exceed 200 characters.")
    private String name;

    @Column(length = 2000)
    //@Size(max = 2000, message = "Description cannot exceed 2000 characters.")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Priority is required.")
    private Priority priority;

    @Column(name = "expected_finish_date")
    private LocalDateTime expectedFinishDate;

    @Column(name = "finished_date")
    private LocalDateTime finishedDate;

    @Column(nullable = false)
    @NotNull(message = "Position is required")
    private Integer position;

    // Relationship with list (many-to-one)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id", nullable = false)
    @NotNull(message = "List is required.")
    private TaskList list;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

    // Métodos de business logic

    public boolean isComplete() {
        return finishedDate != null;
    }

    public void markAsCompleted() {
        this.finishedDate = LocalDateTime.now();
    }

    public void markAsIncomplete() {
        this.finishedDate = null;
    }

    public void updatePosition(Integer newPosition) {
        this.position = newPosition;
    }
}