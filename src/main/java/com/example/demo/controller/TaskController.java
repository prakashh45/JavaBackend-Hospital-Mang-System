package com.example.demo.controller;

import com.example.demo.dto.nurse.TaskRequest;
import com.example.demo.model.TaskItem;
import com.example.demo.service.TaskService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('NURSE','DOCTOR')")
    public List<TaskItem> list(
            @RequestParam(name = "wardId", required = false) String wardId,
            @RequestParam(name = "shiftDate", required = false) LocalDate shiftDate
    ) {
        return taskService.list(wardId, shiftDate);
    }

    @PostMapping
    @PreAuthorize("hasRole('NURSE')")
    public TaskItem create(@RequestBody TaskRequest request) {
        return taskService.create(request);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('NURSE')")
    public TaskItem patch(@PathVariable("id") Long id, @RequestBody TaskRequest request) {
        return taskService.patch(id, request);
    }
}
