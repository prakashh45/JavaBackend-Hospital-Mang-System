package com.example.demo.controller;

import com.example.demo.service.DoctorDashboardService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    private final DoctorDashboardService doctorDashboardService;

    public DoctorController(DoctorDashboardService doctorDashboardService) {
        this.doctorDashboardService = doctorDashboardService;
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('DOCTOR')")
    public Map<String, Object> dashboard() {
        return doctorDashboardService.dashboard();
    }
}
