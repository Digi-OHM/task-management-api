package com.task.manage.entity;

import com.task.manage.Enum.TaskPriority;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskEntity {
    private Long id;
    private String title;
    private String description;
    private TaskPriority priority;
    private LocalDateTime dueDate;
}