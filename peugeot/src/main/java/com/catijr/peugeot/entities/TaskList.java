package com.catijr.peugeot.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Entity
@Table(name = "lists")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TaskList {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(nullable = false, length = 200)
    @NotBlank(message = "List name is required.")
    //@Size(max = 200, message = "List name cannot exceed 200 characters.")
    private String name;

    @Column(nullable = false, name = "\"order\"")
    @NotNull(message = "Order is required.")
    private Integer order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required.")
    private User user;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationship with tasks (one-to-many)
    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private java.util.List<Task> tasks = new ArrayList<>();

    // Business logic

    public void addTask(Task task) {
        tasks.add(task);
        task.setList(this);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
        task.setList(null);
    }

    public void updateOrder(Integer newOrder) {
        this.order = newOrder;
    }
}