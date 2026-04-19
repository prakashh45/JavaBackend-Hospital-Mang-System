package com.example.demo.dto.queue;

import com.example.demo.model.QueueStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueueResponseDto {
    private Long id;
    private Long patientId;
    private String patientName;
    private Integer tokenNumber;
    private String department;
    private QueueStatus status;
    private LocalDateTime queuedAt;
    private String remarks;
}
