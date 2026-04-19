package com.example.demo.controller;

import com.example.demo.dto.vitals.VitalsRequestDto;
import com.example.demo.dto.vitals.VitalsResponseDto;
import com.example.demo.service.VitalsService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/vitals")
public class VitalsController {

    private final VitalsService vitalsService;

    public VitalsController(VitalsService vitalsService) {
        this.vitalsService = vitalsService;
    }

    @PostMapping
    @PreAuthorize("hasRole('NURSE')")
    public VitalsResponseDto createVitals(@Valid @RequestBody VitalsRequestDto requestDto) {
        return vitalsService.createVitals(requestDto);
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('DOCTOR','NURSE','PATIENT')")
    public List<VitalsResponseDto> getVitalsByPatient(@PathVariable Long patientId) {
        return vitalsService.getVitalsByPatient(patientId);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('NURSE')")
    public VitalsResponseDto updateVitals(@PathVariable("id") Long vitalsId, @Valid @RequestBody VitalsRequestDto requestDto) {
        return vitalsService.updateVitals(vitalsId, requestDto);
    }
}
