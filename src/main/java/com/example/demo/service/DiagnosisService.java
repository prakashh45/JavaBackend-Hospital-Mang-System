package com.example.demo.service;

import com.example.demo.dto.diagnosis.DiagnosisRequestDto;
import com.example.demo.dto.diagnosis.DiagnosisResponseDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Diagnosis;
import com.example.demo.model.Patient;
import com.example.demo.repository.DiagnosisRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DiagnosisService {

    private final DiagnosisRepository diagnosisRepository;
    private final PatientService patientService;

    public DiagnosisService(DiagnosisRepository diagnosisRepository, PatientService patientService) {
        this.diagnosisRepository = diagnosisRepository;
        this.patientService = patientService;
    }

    public DiagnosisResponseDto createDiagnosis(DiagnosisRequestDto requestDto) {
        Patient patient = patientService.fetchPatientOrThrow(requestDto.getPatientId());

        Diagnosis diagnosis = Diagnosis.builder()
                .patient(patient)
                .diagnosisDate(requestDto.getDiagnosisDate())
                .conditionName(requestDto.getConditionName().trim())
                .notes(requestDto.getNotes())
                .doctorName(requestDto.getDoctorName().trim())
                .build();

        return toResponse(diagnosisRepository.save(diagnosis));
    }

    @Transactional(readOnly = true)
    public List<DiagnosisResponseDto> getDiagnosesByPatient(Long patientId) {
        patientService.fetchPatientOrThrow(patientId);
        return diagnosisRepository.findByPatientIdOrderByDiagnosisDateDesc(patientId)
                .stream()
                .map(DiagnosisService::toResponse)
                .toList();
    }

    public DiagnosisResponseDto updateDiagnosis(Long diagnosisId, DiagnosisRequestDto requestDto) {
        Diagnosis diagnosis = fetchDiagnosisOrThrow(diagnosisId);
        Patient patient = patientService.fetchPatientOrThrow(requestDto.getPatientId());

        diagnosis.setPatient(patient);
        diagnosis.setDiagnosisDate(requestDto.getDiagnosisDate());
        diagnosis.setConditionName(requestDto.getConditionName().trim());
        diagnosis.setNotes(requestDto.getNotes());
        diagnosis.setDoctorName(requestDto.getDoctorName().trim());

        return toResponse(diagnosisRepository.save(diagnosis));
    }

    public void deleteDiagnosis(Long diagnosisId) {
        Diagnosis diagnosis = fetchDiagnosisOrThrow(diagnosisId);
        diagnosisRepository.delete(diagnosis);
    }

    private Diagnosis fetchDiagnosisOrThrow(Long diagnosisId) {
        return diagnosisRepository.findById(diagnosisId)
                .orElseThrow(() -> new ResourceNotFoundException("Diagnosis not found with id: " + diagnosisId));
    }

    static DiagnosisResponseDto toResponse(Diagnosis diagnosis) {
        return DiagnosisResponseDto.builder()
                .id(diagnosis.getId())
                .patientId(diagnosis.getPatient().getId())
                .patientName(diagnosis.getPatient().getFirstName() + " " + diagnosis.getPatient().getLastName())
                .diagnosisDate(diagnosis.getDiagnosisDate())
                .conditionName(diagnosis.getConditionName())
                .notes(diagnosis.getNotes())
                .doctorName(diagnosis.getDoctorName())
                .build();
    }
}
