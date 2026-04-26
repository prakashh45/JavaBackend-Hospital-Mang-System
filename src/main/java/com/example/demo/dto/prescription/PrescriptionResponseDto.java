package com.example.demo.dto.prescription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionResponseDto {
    private Long id;
    private Long patientId;
    private String patientName;
    private LocalDate prescribedDate;
    private String instructions;
    private String doctorName;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<MedicineResponseDto> medicines;
}
