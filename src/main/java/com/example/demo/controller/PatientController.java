package com.example.demo.controller;

import com.example.demo.dto.patient.PatientHistoryResponseDto;
import com.example.demo.dto.patient.PatientPatchRequestDto;
import com.example.demo.dto.patient.PatientRequestDto;
import com.example.demo.dto.patient.PatientResponseDto;
import com.example.demo.dto.diagnosis.DiagnosisResponseDto;
import com.example.demo.dto.prescription.PrescriptionResponseDto;
import com.example.demo.dto.vitals.VitalsResponseDto;
import com.example.demo.service.PatientService;
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
    public List<PatientResponseDto> getAllPatients(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "limit", required = false) Integer limit
    ) {
        if (search != null || status != null || page != null || limit != null) {
            return patientService.getAllPatients(search, status, page, limit);
        }
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

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR','NURSE')")
    public PatientResponseDto patchPatient(@PathVariable("id") Long patientId, @RequestBody PatientPatchRequestDto requestDto) {
        return patientService.patchPatient(patientId, requestDto);
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

    @GetMapping("/{id}/vitals/current")
    @PreAuthorize("hasAnyRole('DOCTOR','NURSE','PATIENT')")
    public VitalsResponseDto getCurrentVitals(@PathVariable("id") Long patientId) {
        return patientService.getCurrentVitals(patientId);
    }

    @GetMapping("/{id}/diagnoses")
    @PreAuthorize("hasAnyRole('DOCTOR','NURSE','PATIENT')")
    public List<DiagnosisResponseDto> getPatientDiagnoses(@PathVariable("id") Long patientId) {
        return patientService.getPatientDiagnoses(patientId);
    }

    @GetMapping("/{id}/prescriptions")
    @PreAuthorize("hasAnyRole('DOCTOR','NURSE','PATIENT')")
    public List<PrescriptionResponseDto> getPatientPrescriptions(@PathVariable("id") Long patientId) {
        return patientService.getPatientPrescriptions(patientId);
    }
}
