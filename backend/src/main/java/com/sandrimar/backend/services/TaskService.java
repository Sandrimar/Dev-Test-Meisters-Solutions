package com.sandrimar.backend.services;

import com.sandrimar.backend.dto.TaskRequestDTO;
import com.sandrimar.backend.dto.TaskResponseDTO;
import com.sandrimar.backend.model.Task;
import com.sandrimar.backend.model.TaskStatus;
import com.sandrimar.backend.repositories.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public TaskResponseDTO createTask(TaskRequestDTO body) {
        if (LocalDate.now().getDayOfWeek().equals(DayOfWeek.SATURDAY)
                || LocalDate.now().getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            throw new IllegalStateException("Tasks can only be created during weekdays");
        }
        Task createdTask = repository.save(new Task(body));
        return new TaskResponseDTO(createdTask);
    }

    public List<TaskResponseDTO> getAllTasks() {
        return repository.findAll().stream()
                .map(TaskResponseDTO::new).toList();
    }

    public TaskResponseDTO updateTask(Long id, TaskRequestDTO body) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        if (task.getStatus() != TaskStatus.PENDING) {
            throw new IllegalStateException("Tasks can only be updated if in status pending");
        }
        task.setTitle(body.title());
        task.setDescription(body.description());
        repository.save(task);
        return new TaskResponseDTO(task);
    }

    public void updateTaskStatus(Long id, String status) {
        try {
            TaskStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Invalid Status");
        }
        Task task = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setStatus(TaskStatus.valueOf(status));
        repository.save(task);
    }

    public void deleteTask(Long id) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        if (task.getStatus() != TaskStatus.PENDING
                || task.getCreationDate().isAfter(LocalDate.now().minusDays(5))) {
            throw new IllegalStateException("Tasks can only be deleted if in status pending and older than 5 days");
        }
        repository.delete(task);
    }
}
