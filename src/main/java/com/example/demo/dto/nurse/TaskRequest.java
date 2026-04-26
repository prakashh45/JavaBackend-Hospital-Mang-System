package com.example.demo.dto.nurse;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class TaskRequest {
    private String title;
    private String description;
    private String wardId;
    private LocalDate shiftDate;
    private LocalDateTime dueAt;
    private String assignedTo;
    private String status;
    private String priority;
}
