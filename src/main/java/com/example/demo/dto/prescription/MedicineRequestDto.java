package com.example.demo.dto.prescription;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineRequestDto {

    @NotBlank(message = "Medicine name is required")
    @Size(max = 160, message = "Medicine name can be at most 160 characters")
    private String medicineName;

    @NotBlank(message = "Dosage is required")
    @Size(max = 120, message = "Dosage can be at most 120 characters")
    private String dosage;

    @NotBlank(message = "Frequency is required")
    @Size(max = 120, message = "Frequency can be at most 120 characters")
    private String frequency;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 day")
    private Integer durationDays;
}
