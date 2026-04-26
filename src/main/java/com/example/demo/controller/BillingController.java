package com.example.demo.controller;

import com.example.demo.dto.billing.InvoiceItemRequest;
import com.example.demo.dto.billing.InvoiceRequest;
import com.example.demo.dto.patient.PatientResponseDto;
import com.example.demo.model.InsuranceProvider;
import com.example.demo.service.BillingService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import java.util.Map;

@RestController
@RequestMapping("/billing")
public class BillingController {

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @GetMapping("/patients/search")
    @PreAuthorize("hasAnyRole('DOCTOR','NURSE')")
    public List<PatientResponseDto> searchPatients(@RequestParam(name = "q", required = false) String q) {
        return billingService.searchPatients(q);
    }

    @GetMapping("/invoices/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR','NURSE')")
    public Map<String, Object> getInvoice(@PathVariable("id") Long invoiceId) {
        return billingService.getInvoice(invoiceId);
    }

    @PostMapping("/invoices")
    @PreAuthorize("hasAnyRole('DOCTOR','NURSE')")
    public Map<String, Object> createInvoice(@RequestBody InvoiceRequest request) {
        return billingService.createInvoice(request);
    }

    @PutMapping("/invoices/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR','NURSE')")
    public Map<String, Object> updateInvoice(@PathVariable("id") Long invoiceId, @RequestBody InvoiceRequest request) {
        return billingService.updateInvoice(invoiceId, request);
    }

    @PostMapping("/invoices/{id}/items")
    @PreAuthorize("hasAnyRole('DOCTOR','NURSE')")
    public Map<String, Object> addItem(@PathVariable("id") Long invoiceId, @RequestBody InvoiceItemRequest request) {
        return billingService.addItem(invoiceId, request);
    }

    @PutMapping("/invoices/{id}/items/{itemId}")
    @PreAuthorize("hasAnyRole('DOCTOR','NURSE')")
    public Map<String, Object> updateItem(
            @PathVariable("id") Long invoiceId,
            @PathVariable("itemId") Long itemId,
            @RequestBody InvoiceItemRequest request
    ) {
        return billingService.updateItem(invoiceId, itemId, request);
    }

    @DeleteMapping("/invoices/{id}/items/{itemId}")
    @PreAuthorize("hasAnyRole('DOCTOR','NURSE')")
    public Map<String, Object> deleteItem(@PathVariable("id") Long invoiceId, @PathVariable("itemId") Long itemId) {
        return billingService.deleteItem(invoiceId, itemId);
    }

    @PostMapping("/invoices/{id}/draft")
    @PreAuthorize("hasAnyRole('DOCTOR','NURSE')")
    public Map<String, Object> markDraft(@PathVariable("id") Long invoiceId) {
        return billingService.markDraft(invoiceId);
    }

    @PostMapping("/invoices/{id}/send")
    @PreAuthorize("hasAnyRole('DOCTOR','NURSE')")
    public Map<String, Object> send(@PathVariable("id") Long invoiceId) {
        return billingService.send(invoiceId);
    }

    @GetMapping("/invoices/{id}/pdf")
    @PreAuthorize("hasAnyRole('DOCTOR','NURSE')")
    public ResponseEntity<byte[]> pdf(@PathVariable("id") Long invoiceId) {
        byte[] content = billingService.invoicePdf(invoiceId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice-" + invoiceId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(content);
    }
}
