package com.example.demo.controller;

import com.example.demo.dto.patient.PatientHistoryResponseDto;
import com.example.demo.dto.patient.PatientRequestDto;
import com.example.demo.dto.patient.PatientResponseDto;
import com.example.demo.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('DOCTOR','NURSE')")
    public PatientResponseDto createPatient(@Valid @RequestBody PatientRequestDto requestDto) {
        return patientService.createPatient(requestDto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR','NURSE','PATIENT')")
    public List<PatientResponseDto> getAllPatients() {
        return patientService.getAllPatients();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR','NURSE','PATIENT')")
    public PatientResponseDto getPatientById(@PathVariable("id") Long patientId) {
        return patientService.getPatientById(patientId);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('DOCTOR','NURSE','PATIENT')")
    public List<PatientResponseDto> searchPatients(@RequestParam(name = "query", required = false) String query) {
        return patientService.searchPatients(query);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR','NURSE')")
    public PatientResponseDto updatePatient(@PathVariable("id") Long patientId, @Valid @RequestBody PatientRequestDto requestDto) {
        return patientService.updatePatient(patientId, requestDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR','NURSE')")
    public void deletePatient(@PathVariable("id") Long patientId) {
        patientService.deletePatient(patientId);
    }

    @GetMapping("/{id}/history")
    @PreAuthorize("hasAnyRole('DOCTOR','NURSE','PATIENT')")
    public PatientHistoryResponseDto getPatientHistory(@PathVariable("id") Long patientId) {
        return patientService.getPatientHistory(patientId);
    }
}
