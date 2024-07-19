package com.sandrimar.backend.controllers;

import com.sandrimar.backend.dto.TaskRequestDTO;
import com.sandrimar.backend.dto.TaskResponseDTO;
import com.sandrimar.backend.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody TaskRequestDTO body) {
        TaskResponseDTO createdTask = service.createTask(body);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdTask.id()).toUri();
        return ResponseEntity.created(uri).body(createdTask);
    }
}
