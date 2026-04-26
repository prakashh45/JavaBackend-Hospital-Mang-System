package com.example.demo.service;

import com.example.demo.dto.nurse.TaskRequest;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.TaskItem;
import com.example.demo.repository.TaskItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class TaskService {

    private final TaskItemRepository taskItemRepository;

    public TaskService(TaskItemRepository taskItemRepository) {
        this.taskItemRepository = taskItemRepository;
    }

    @Transactional(readOnly = true)
    public List<TaskItem> list(String wardId, LocalDate shiftDate) {
        if (wardId != null && !wardId.isBlank() && shiftDate != null) {
            return taskItemRepository.findByWardIdAndShiftDateOrderByCreatedAtDesc(wardId, shiftDate);
        }
        if (wardId != null && !wardId.isBlank()) {
            return taskItemRepository.findByWardIdOrderByCreatedAtDesc(wardId);
        }
        if (shiftDate != null) {
            return taskItemRepository.findByShiftDateOrderByCreatedAtDesc(shiftDate);
        }
        return taskItemRepository.findAll();
    }

    public TaskItem create(TaskRequest request) {
        TaskItem task = TaskItem.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .wardId(request.getWardId())
                .shiftDate(request.getShiftDate())
                .dueAt(request.getDueAt())
                .assignedTo(request.getAssignedTo())
                .status(request.getStatus())
                .priority(request.getPriority())
                .build();
        return taskItemRepository.save(task);
    }

    public TaskItem patch(Long id, TaskRequest request) {
        TaskItem task = taskItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        if (request.getTitle() != null) task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());
        if (request.getWardId() != null) task.setWardId(request.getWardId());
        if (request.getShiftDate() != null) task.setShiftDate(request.getShiftDate());
        if (request.getDueAt() != null) task.setDueAt(request.getDueAt());
        if (request.getAssignedTo() != null) task.setAssignedTo(request.getAssignedTo());
        if (request.getStatus() != null) task.setStatus(request.getStatus().toUpperCase());
        if (request.getPriority() != null) task.setPriority(request.getPriority().toUpperCase());

        return taskItemRepository.save(task);
    }
}
