package com.example.demo.controller;

import com.example.demo.dto.common.StatusUpdateRequest;
import com.example.demo.dto.diagnosis.DiagnosisRequestDto;
import com.example.demo.dto.diagnosis.DiagnosisResponseDto;
import com.example.demo.service.DiagnosisService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"/diagnosis", "/diagnoses"})
public class DiagnosisController {

    private final DiagnosisService diagnosisService;

    public DiagnosisController(DiagnosisService diagnosisService) {
        this.diagnosisService = diagnosisService;
    }

    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public DiagnosisResponseDto createDiagnosis(@Valid @RequestBody DiagnosisRequestDto requestDto) {
        return diagnosisService.createDiagnosis(requestDto);
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('DOCTOR','PATIENT')")
    public List<DiagnosisResponseDto> getDiagnosesByPatient(@PathVariable Long patientId) {
        return diagnosisService.getDiagnosesByPatient(patientId);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR','PATIENT')")
    public List<DiagnosisResponseDto> getDiagnoses(
            @RequestParam(name = "patientId", required = false) Long patientId,
            @RequestParam(name = "priority", required = false) String priority,
            @RequestParam(name = "status", required = false) String status
    ) {
        return diagnosisService.getDiagnoses(patientId, priority, status);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public DiagnosisResponseDto updateDiagnosis(@PathVariable("id") Long diagnosisId, @Valid @RequestBody DiagnosisRequestDto requestDto) {
        return diagnosisService.updateDiagnosis(diagnosisId, requestDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public void deleteDiagnosis(@PathVariable("id") Long diagnosisId) {
        diagnosisService.deleteDiagnosis(diagnosisId);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('DOCTOR')")
    public DiagnosisResponseDto patchStatus(@PathVariable("id") Long diagnosisId, @Valid @RequestBody StatusUpdateRequest request) {
        return diagnosisService.updateStatus(diagnosisId, request.getStatus());
    }
}
