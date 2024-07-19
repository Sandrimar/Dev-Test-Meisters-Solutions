package com.sandrimar.backend.controllers;

import com.sandrimar.backend.dto.TaskRequestDTO;
import com.sandrimar.backend.dto.TaskResponseDTO;
import com.sandrimar.backend.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks() {
        List<TaskResponseDTO> allTasks = service.getAllTasks();
        return ResponseEntity.ok().body(allTasks);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequestDTO body) {
        TaskResponseDTO updatedTask = service.updateTask(id, body);
        return ResponseEntity.ok().body(updatedTask);
    }
}
