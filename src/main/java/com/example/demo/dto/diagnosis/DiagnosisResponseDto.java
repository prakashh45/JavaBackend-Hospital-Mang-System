package com.example.demo.dto.diagnosis;

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
public class DiagnosisResponseDto {
    private Long id;
    private Long patientId;
    private String patientName;
    private LocalDate diagnosisDate;
    private String conditionName;
    private String notes;
    private String doctorName;
}
