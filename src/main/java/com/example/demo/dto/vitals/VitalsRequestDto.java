package com.example.demo.dto.vitals;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
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
public class VitalsRequestDto {

    @NotNull(message = "Patient id is required")
    private Long patientId;

    @NotNull(message = "Recorded at timestamp is required")
    private LocalDateTime recordedAt;

    @NotNull(message = "Temperature is required")
    @DecimalMin(value = "30.0", message = "Temperature must be at least 30.0")
    @DecimalMax(value = "45.0", message = "Temperature must be at most 45.0")
    private Double temperature;

    @NotBlank(message = "Blood pressure is required")
    @Size(max = 20, message = "Blood pressure can be at most 20 characters")
    private String bloodPressure;

    @NotNull(message = "Pulse is required")
    @Min(value = 20, message = "Pulse must be at least 20")
    @Max(value = 250, message = "Pulse must be at most 250")
    private Integer pulse;

    @NotNull(message = "Respiratory rate is required")
    @Min(value = 5, message = "Respiratory rate must be at least 5")
    @Max(value = 80, message = "Respiratory rate must be at most 80")
    private Integer respiratoryRate;

    @NotNull(message = "Oxygen saturation is required")
    @Min(value = 50, message = "Oxygen saturation must be at least 50")
    @Max(value = 100, message = "Oxygen saturation must be at most 100")
    private Integer oxygenSaturation;

    @NotBlank(message = "Nurse name is required")
    @Size(max = 120, message = "Nurse name can be at most 120 characters")
    private String nurseName;
}
