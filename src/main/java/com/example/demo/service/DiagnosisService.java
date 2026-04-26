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
                .priority(normalize(requestDto.getPriority(), "NORMAL"))
                .status(normalize(requestDto.getStatus(), "OPEN"))
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

    @Transactional(readOnly = true)
    public List<DiagnosisResponseDto> getDiagnoses(Long patientId, String priority, String status) {
        List<Diagnosis> diagnoses;

        boolean hasPatient = patientId != null;
        boolean hasPriority = priority != null && !priority.isBlank();
        boolean hasStatus = status != null && !status.isBlank();

        if (hasPatient && hasPriority && hasStatus) {
            diagnoses = diagnosisRepository.findByPatientIdAndPriorityIgnoreCaseAndStatusIgnoreCaseOrderByDiagnosisDateDesc(
                    patientId, priority, status
            );
        } else if (hasPatient && hasPriority) {
            diagnoses = diagnosisRepository.findByPatientIdAndPriorityIgnoreCaseOrderByDiagnosisDateDesc(patientId, priority);
        } else if (hasPatient && hasStatus) {
            diagnoses = diagnosisRepository.findByPatientIdAndStatusIgnoreCaseOrderByDiagnosisDateDesc(patientId, status);
        } else if (hasPriority && hasStatus) {
            diagnoses = diagnosisRepository.findByPriorityIgnoreCaseAndStatusIgnoreCaseOrderByDiagnosisDateDesc(priority, status);
        } else if (hasPriority) {
            diagnoses = diagnosisRepository.findByPriorityIgnoreCaseOrderByDiagnosisDateDesc(priority);
        } else if (hasStatus) {
            diagnoses = diagnosisRepository.findByStatusIgnoreCaseOrderByDiagnosisDateDesc(status);
        } else if (hasPatient) {
            diagnoses = diagnosisRepository.findByPatientIdOrderByDiagnosisDateDesc(patientId);
        } else {
            diagnoses = diagnosisRepository.findAllByOrderByDiagnosisDateDesc();
        }

        return diagnoses.stream().map(DiagnosisService::toResponse).toList();
    }

    public DiagnosisResponseDto updateDiagnosis(Long diagnosisId, DiagnosisRequestDto requestDto) {
        Diagnosis diagnosis = fetchDiagnosisOrThrow(diagnosisId);
        Patient patient = patientService.fetchPatientOrThrow(requestDto.getPatientId());

        diagnosis.setPatient(patient);
        diagnosis.setDiagnosisDate(requestDto.getDiagnosisDate());
        diagnosis.setConditionName(requestDto.getConditionName().trim());
        diagnosis.setNotes(requestDto.getNotes());
        diagnosis.setDoctorName(requestDto.getDoctorName().trim());
        diagnosis.setPriority(normalize(requestDto.getPriority(), diagnosis.getPriority()));
        diagnosis.setStatus(normalize(requestDto.getStatus(), diagnosis.getStatus()));

        return toResponse(diagnosisRepository.save(diagnosis));
    }

    public DiagnosisResponseDto updateStatus(Long diagnosisId, String status) {
        Diagnosis diagnosis = fetchDiagnosisOrThrow(diagnosisId);
        diagnosis.setStatus(normalize(status, diagnosis.getStatus()));
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
                .priority(diagnosis.getPriority())
                .status(diagnosis.getStatus())
                .build();
    }

    private String normalize(String value, String fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        return value.trim().toUpperCase();
    }
}
