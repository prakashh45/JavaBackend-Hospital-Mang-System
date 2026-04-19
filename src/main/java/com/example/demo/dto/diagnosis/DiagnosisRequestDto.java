package com.example.demo.dto.diagnosis;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiagnosisRequestDto {

    @NotNull(message = "Patient id is required")
    private Long patientId;

    @NotNull(message = "Diagnosis date is required")
    private LocalDate diagnosisDate;

    @NotBlank(message = "Condition name is required")
    @Size(max = 200, message = "Condition name can be at most 200 characters")
    private String conditionName;

    @Size(max = 1000, message = "Notes can be at most 1000 characters")
    private String notes;

    @NotBlank(message = "Doctor name is required")
    @Size(max = 120, message = "Doctor name can be at most 120 characters")
    private String doctorName;
}
