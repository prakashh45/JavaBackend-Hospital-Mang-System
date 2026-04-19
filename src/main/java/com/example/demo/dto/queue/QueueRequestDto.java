package com.example.demo.dto.queue;

import com.example.demo.model.QueueStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class QueueRequestDto {

    @NotNull(message = "Patient id is required")
    private Long patientId;

    @NotNull(message = "Token number is required")
    @Min(value = 1, message = "Token number must be at least 1")
    private Integer tokenNumber;

    @NotBlank(message = "Department is required")
    @Size(max = 120, message = "Department can be at most 120 characters")
    private String department;

    private QueueStatus status;

    private LocalDateTime queuedAt;

    @Size(max = 500, message = "Remarks can be at most 500 characters")
    private String remarks;
}
