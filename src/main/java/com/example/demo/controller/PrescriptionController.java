package com.example.demo.controller;

import com.example.demo.dto.prescription.PrescriptionRequestDto;
import com.example.demo.dto.prescription.PrescriptionResponseDto;
import com.example.demo.service.PrescriptionService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public PrescriptionResponseDto createPrescription(@Valid @RequestBody PrescriptionRequestDto requestDto) {
        return prescriptionService.createPrescription(requestDto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR','PATIENT')")
    public PrescriptionResponseDto getPrescriptionById(@PathVariable("id") Long prescriptionId) {
        return prescriptionService.getPrescriptionById(prescriptionId);
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('DOCTOR','PATIENT')")
    public List<PrescriptionResponseDto> getPrescriptionsByPatient(@PathVariable Long patientId) {
        return prescriptionService.getPrescriptionsByPatient(patientId);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public PrescriptionResponseDto updatePrescription(@PathVariable("id") Long prescriptionId, @Valid @RequestBody PrescriptionRequestDto requestDto) {
        return prescriptionService.updatePrescription(prescriptionId, requestDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public void deletePrescription(@PathVariable("id") Long prescriptionId) {
        prescriptionService.deletePrescription(prescriptionId);
    }
}
