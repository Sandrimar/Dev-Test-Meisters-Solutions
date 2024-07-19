package com.sandrimar.backend.services;

import com.sandrimar.backend.dto.TaskRequestDTO;
import com.sandrimar.backend.dto.TaskResponseDTO;
import com.sandrimar.backend.model.Task;
import com.sandrimar.backend.repositories.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;

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
}
