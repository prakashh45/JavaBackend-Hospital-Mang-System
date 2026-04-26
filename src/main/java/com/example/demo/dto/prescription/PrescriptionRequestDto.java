package com.example.demo.dto.prescription;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionRequestDto {

    @NotNull(message = "Patient id is required")
    private Long patientId;

    @NotNull(message = "Prescribed date is required")
    private LocalDate prescribedDate;

    @NotBlank(message = "Instructions are required")
    @Size(max = 1000, message = "Instructions can be at most 1000 characters")
    private String instructions;

    @NotBlank(message = "Doctor name is required")
    @Size(max = 120, message = "Doctor name can be at most 120 characters")
    private String doctorName;

    @Size(max = 20, message = "Status can be at most 20 characters")
    private String status;

    @NotEmpty(message = "At least one medicine is required")
    private List<@Valid MedicineRequestDto> medicines;
}
