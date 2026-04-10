package com.task.manage.controller;

import com.task.manage.Enum.SortEnum;
import com.task.manage.Enum.TaskPriority;
import com.task.manage.entity.TaskEntity;
import com.task.manage.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping("/{id}")
    public ResponseEntity<TaskEntity> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.findById(id).orElse(null));
    }

    @GetMapping("/page")
    public ResponseEntity<Page<TaskEntity>> getTaskPageable(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "ASC") SortEnum sort, @RequestParam(required = false) TaskPriority priority) {
        return ResponseEntity.ok(taskService.getTaskPageable(page, size, sort, priority));
    }

    @PostMapping("/add")
    public ResponseEntity<TaskEntity> addTask(@RequestBody TaskEntity request) {
        return ResponseEntity.ok(taskService.upsertTask(request));
    }

    @PutMapping("/edit")
    public ResponseEntity<TaskEntity> updateTask(@RequestBody TaskEntity request) {
        return ResponseEntity.ok(taskService.upsertTask(request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.deleteTask(id));
    }

    @DeleteMapping("/delete-multiple")
    public ResponseEntity<String> deleteMultipleTasks(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(taskService.deleteMultipleTasks(ids));
    }

    @PatchMapping("/priority/{id}")
    public ResponseEntity<TaskEntity> updatePriority(@PathVariable Long id, @RequestParam TaskPriority priority) {
        return ResponseEntity.ok(taskService.updateTaskPartialOrCompleted(id, priority));
    }

}
