package com.example.demo.controller;

import com.example.demo.model.InsuranceProvider;
import com.example.demo.service.BillingService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/insurance")
public class InsuranceController {

    private final BillingService billingService;

    public InsuranceController(BillingService billingService) {
        this.billingService = billingService;
    }

    @GetMapping("/providers")
    @PreAuthorize("hasAnyRole('DOCTOR','NURSE')")
    public List<InsuranceProvider> providers() {
        return billingService.insuranceProviders();
    }
}
