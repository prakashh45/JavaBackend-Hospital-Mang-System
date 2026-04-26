package com.example.demo.controller;

import com.example.demo.dto.nurse.NurseNoteRequest;
import com.example.demo.dto.patient.PatientResponseDto;
import com.example.demo.service.NurseService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/nurse")
public class NurseController {

    private final NurseService nurseService;

    public NurseController(NurseService nurseService) {
        this.nurseService = nurseService;
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('NURSE')")
    public Map<String, Object> dashboard() {
        return nurseService.dashboard();
    }

    @GetMapping("/ward-patients")
    @PreAuthorize("hasRole('NURSE')")
    public List<PatientResponseDto> wardPatients(
            @RequestParam(name = "wardId", required = false) String wardId,
            @RequestParam(name = "priority", required = false) String priority
    ) {
        return nurseService.wardPatients(wardId, priority);
    }

    @PostMapping("/notes")
    @PreAuthorize("hasRole('NURSE')")
    public Map<String, Object> addNote(@Valid @RequestBody NurseNoteRequest request) {
        return nurseService.addNote(request);
    }
}
