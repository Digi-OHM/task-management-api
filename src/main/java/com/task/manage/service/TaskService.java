package com.task.manage.service;

import com.task.manage.Enum.SortEnum;
import com.task.manage.Enum.TaskPriority;
import com.task.manage.entity.TaskEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskService {
    private final List<TaskEntity> taskList = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public TaskEntity upsertTask(TaskEntity task) {
        if (task.getId() == null || task.getId() == 0) {
            task.setId(idGenerator.getAndIncrement());
            taskList.add(task);
            log.info("Added new task ID: {}", task.getId());
            return task;
        } else {
            return findById(task.getId()).map(existingTask -> {
                int index = taskList.indexOf(existingTask);
                taskList.set(index, task);
                log.info("Updated task ID: {}", task.getId());
                return task;
            }).orElseThrow(() -> new RuntimeException("Task not found with ID: " + task.getId()));
        }
    }

    public Optional<TaskEntity> findById(Long id) {
        return taskList.stream().filter(t -> t.getId().equals(id)).findFirst();
    }

    public String deleteTask(Long id) {
        boolean removed = taskList.removeIf(t -> t.getId().equals(id));
        if (!removed) {
            throw new RuntimeException("Task not found with ID: " + id);
        }
        log.info("Deleted task ID: {}", id);
        return "Deleted task ID: " + id + " successfully";
    }

    public String deleteMultipleTasks(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return "No tasks selected for deletion.";
        }
        int sizeBefore = taskList.size();
        taskList.removeIf(t -> ids.contains(t.getId()));
        int sizeAfter = taskList.size();
        int deletedCount = sizeBefore - sizeAfter;
        return "Deleted " + deletedCount + " tasks successfully from " + ids.size() + " selected.";
    }

    public Page<TaskEntity> getTaskPageable(int page, int size, SortEnum sortDir, TaskPriority priority) {
        var stream = taskList.stream();
        if (priority != null) {
            stream = stream.filter(t -> t.getPriority() == priority);
        }
        stream = stream.sorted((t1, t2) -> {
            if (SortEnum.DESC.equals(sortDir)) {
                return t2.getId().compareTo(t1.getId());
            }
            return t1.getId().compareTo(t2.getId());
        });

        List<TaskEntity> filteredList = stream.toList();
        int start = (int) PageRequest.of(page, size).getOffset();
        int end = Math.min((start + size), filteredList.size());
        List<TaskEntity> pageContent = new ArrayList<>();
        if (start < filteredList.size()) {
            pageContent = filteredList.subList(start, end);
        }

        return new PageImpl<>(pageContent, PageRequest.of(page, size), filteredList.size());
    }

    public TaskEntity updateTaskPartialOrCompleted(Long id, TaskPriority priority) {
        TaskEntity task = findById(id).orElseThrow(() -> new RuntimeException("Task not found with ID: " + id));
        if (priority != null) {
            task.setPriority(priority);
        }
        return task;
    }
}