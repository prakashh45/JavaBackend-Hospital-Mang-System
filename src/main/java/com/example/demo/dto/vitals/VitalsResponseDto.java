package com.example.demo.dto.vitals;

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
public class VitalsResponseDto {
    private Long id;
    private Long patientId;
    private String patientName;
    private LocalDateTime recordedAt;
    private Double temperature;
    private String bloodPressure;
    private Integer pulse;
    private Integer respiratoryRate;
    private Integer oxygenSaturation;
    private String nurseName;
}
