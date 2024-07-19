package com.sandrimar.backend.dto;

import com.sandrimar.backend.model.Task;
import com.sandrimar.backend.model.TaskStatus;

import java.time.LocalDate;

public record TaskResponseDTO(Long id, String title, String description, TaskStatus status, LocalDate creationDate) {
    public TaskResponseDTO(Task task) {
        this(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), task.getCreationDate());
    }
}
