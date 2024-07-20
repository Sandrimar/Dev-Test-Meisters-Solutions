package com.sandrimar.backend.repositories;

import com.sandrimar.backend.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t WHERE t.status = 'PENDING'")
    List<Task> findPendingTasks();
}
