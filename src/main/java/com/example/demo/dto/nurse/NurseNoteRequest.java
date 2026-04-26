package com.example.demo.dto.nurse;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NurseNoteRequest {
    @NotNull
    private Long patientId;
    @NotBlank
    private String nurseName;
    private String wardId;
    @NotBlank
    private String note;
}
