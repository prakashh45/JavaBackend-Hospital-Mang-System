package com.example.demo.dto.patient;

import com.example.demo.dto.diagnosis.DiagnosisResponseDto;
import com.example.demo.dto.prescription.PrescriptionResponseDto;
import com.example.demo.dto.queue.QueueResponseDto;
import com.example.demo.dto.vitals.VitalsResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientHistoryResponseDto {
    private PatientResponseDto patient;
    private List<DiagnosisResponseDto> diagnoses;
    private List<PrescriptionResponseDto> prescriptions;
    private List<VitalsResponseDto> vitals;
    private List<QueueResponseDto> queueHistory;
}
