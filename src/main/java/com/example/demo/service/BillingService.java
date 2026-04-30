package com.example.demo.service;

import com.example.demo.dto.billing.InvoiceItemRequest;
import com.example.demo.dto.billing.InvoiceRequest;
import com.example.demo.dto.patient.PatientResponseDto;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.InsuranceProvider;
import com.example.demo.model.Invoice;
import com.example.demo.model.InvoiceItem;
import com.example.demo.model.Patient;
import com.example.demo.repository.InsuranceProviderRepository;
import com.example.demo.repository.InvoiceItemRepository;
import com.example.demo.repository.InvoiceRepository;
import com.example.demo.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class BillingService {

    private final PatientService patientService;
    private final PatientRepository patientRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemRepository invoiceItemRepository;
    private final InsuranceProviderRepository insuranceProviderRepository;

    public BillingService(
            PatientService patientService,
            PatientRepository patientRepository,
            InvoiceRepository invoiceRepository,
            InvoiceItemRepository invoiceItemRepository,
            InsuranceProviderRepository insuranceProviderRepository
    ) {
        this.patientService = patientService;
        this.patientRepository = patientRepository;
        this.invoiceRepository = invoiceRepository;
        this.invoiceItemRepository = invoiceItemRepository;
        this.insuranceProviderRepository = insuranceProviderRepository;
    }

    @Transactional(readOnly = true)
    public List<PatientResponseDto> searchPatients(String q) {
        String query = q == null ? "" : q.trim();
        return patientRepository.search(query)
                .stream()
                .map(p -> PatientResponseDto.builder()
                        .id(p.getId())
                        .firstName(p.getFirstName())
                        .lastName(p.getLastName())
                        .gender(p.getGender())
                        .dateOfBirth(p.getDateOfBirth())
                        .phone(p.getPhone())
                        .email(p.getEmail())
                        .address(p.getAddress())
                        .status(p.getStatus())
                        .wardId(p.getWardId())
                        .priority(p.getPriority())
                        .createdAt(p.getCreatedAt())
                        .updatedAt(p.getUpdatedAt())
                        .build())
                .toList();
    }

    public List<InsuranceProvider> insuranceProviders() {
        List<InsuranceProvider> existing = insuranceProviderRepository.findAll();
        if (existing.isEmpty()) {
            insuranceProviderRepository.saveAll(List.of(
                    InsuranceProvider.builder().code("NIVA").name("Niva Bupa").contactNumber("+91-0000000001").build(),
                    InsuranceProvider.builder().code("STAR").name("Star Health").contactNumber("+91-0000000002").build(),
                    InsuranceProvider.builder().code("HDFC").name("HDFC Ergo").contactNumber("+91-0000000003").build()
            ));
            existing = insuranceProviderRepository.findAll();
        }
        return existing;
    }

    public Map<String, Object> createInvoice(InvoiceRequest request) {
        Patient patient = patientService.fetchPatientOrThrow(request.getPatientId());
        String invoiceNumber = request.getInvoiceNumber();
        if (invoiceNumber == null || invoiceNumber.isBlank()) {
            invoiceNumber = "INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }

        if (invoiceRepository.findByInvoiceNumber(invoiceNumber).isPresent()) {
            throw new BadRequestException("Invoice number already exists");
        }

        Invoice invoice = Invoice.builder()
                .invoiceNumber(invoiceNumber)
                .patient(patient)
                .status("DRAFT")
                .currency(request.getCurrency())
                .tax(request.getTax() == null ? 0.0 : request.getTax())
                .subtotal(0.0)
                .total(0.0)
                .notes(request.getNotes())
                .build();

        return toInvoiceMap(invoiceRepository.save(invoice));
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getInvoice(Long invoiceId) {
        return toInvoiceMap(fetchInvoice(invoiceId));
    }

    public Map<String, Object> updateInvoice(Long invoiceId, InvoiceRequest request) {
        Invoice invoice = fetchInvoice(invoiceId);

        if (request.getCurrency() != null) invoice.setCurrency(request.getCurrency().trim().toUpperCase());
        if (request.getTax() != null) invoice.setTax(request.getTax());
        if (request.getNotes() != null) invoice.setNotes(request.getNotes());
        if (request.getPatientId() != null) {
            Patient patient = patientService.fetchPatientOrThrow(request.getPatientId());
            invoice.setPatient(patient);
        }

        recalculateTotals(invoice);
        return toInvoiceMap(invoiceRepository.save(invoice));
    }

    public Map<String, Object> addItem(Long invoiceId, InvoiceItemRequest request) {
        Invoice invoice = fetchInvoice(invoiceId);
        InvoiceItem item = InvoiceItem.builder()
                .invoice(invoice)
                .itemName(request.getItemName())
                .quantity(request.getQuantity())
                .unitPrice(request.getUnitPrice())
                .build();
        invoiceItemRepository.save(item);
        invoice.getItems().add(item);
        recalculateTotals(invoice);
        invoiceRepository.save(invoice);
        return toInvoiceMap(invoice);
    }

    public Map<String, Object> updateItem(Long invoiceId, Long itemId, InvoiceItemRequest request) {
        Invoice invoice = fetchInvoice(invoiceId);
        InvoiceItem item = invoiceItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice item not found"));
        if (!item.getInvoice().getId().equals(invoice.getId())) {
            throw new BadRequestException("Item does not belong to invoice");
        }

        if (request.getItemName() != null) item.setItemName(request.getItemName());
        if (request.getQuantity() != null) item.setQuantity(request.getQuantity());
        if (request.getUnitPrice() != null) item.setUnitPrice(request.getUnitPrice());
        invoiceItemRepository.save(item);

        recalculateTotals(invoice);
        invoiceRepository.save(invoice);
        return toInvoiceMap(invoice);
    }

    public Map<String, Object> deleteItem(Long invoiceId, Long itemId) {
        Invoice invoice = fetchInvoice(invoiceId);
        InvoiceItem item = invoiceItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice item not found"));
        if (!item.getInvoice().getId().equals(invoice.getId())) {
            throw new BadRequestException("Item does not belong to invoice");
        }
        invoice.getItems().removeIf(it -> it.getId().equals(itemId));
        invoiceItemRepository.delete(item);
        recalculateTotals(invoice);
        invoiceRepository.save(invoice);
        return toInvoiceMap(invoice);
    }

    public Map<String, Object> markDraft(Long invoiceId) {
        Invoice invoice = fetchInvoice(invoiceId);
        invoice.setStatus("DRAFT");
        invoice.setDraftAt(LocalDateTime.now());
        return toInvoiceMap(invoiceRepository.save(invoice));
    }

    public Map<String, Object> send(Long invoiceId) {
        Invoice invoice = fetchInvoice(invoiceId);
        invoice.setStatus("SENT");
        invoice.setSentAt(LocalDateTime.now());
        return toInvoiceMap(invoiceRepository.save(invoice));
    }

    @Transactional(readOnly = true)
    public byte[] invoicePdf(Long invoiceId) {
        Invoice invoice = fetchInvoice(invoiceId);
        String content = "Invoice " + invoice.getInvoiceNumber() + "\nPatient: " +
                invoice.getPatient().getFirstName() + " " + invoice.getPatient().getLastName() +
                "\nTotal: " + invoice.getTotal() + " " + invoice.getCurrency();
        return content.getBytes(StandardCharsets.UTF_8);
    }

    private Invoice fetchInvoice(Long invoiceId) {
        return invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + invoiceId));
    }

    private void recalculateTotals(Invoice invoice) {
        double subtotal = invoice.getItems().stream()
                .mapToDouble(item -> {
                    int qty = item.getQuantity() == null ? 0 : item.getQuantity();
                    double price = item.getUnitPrice() == null ? 0 : item.getUnitPrice();
                    return qty * price;
                })
                .sum();
        invoice.setSubtotal(subtotal);
        double tax = invoice.getTax() == null ? 0.0 : invoice.getTax();
        invoice.setTotal(subtotal + tax);
    }

    private Map<String, Object> toInvoiceMap(Invoice invoice) {
        List<Map<String, Object>> items = invoice.getItems().stream()
                .map(item -> {
                    Map<String, Object> itemMap = new LinkedHashMap<>();
                    itemMap.put("id", item.getId());
                    itemMap.put("itemName", item.getItemName());
                    itemMap.put("quantity", item.getQuantity());
                    itemMap.put("unitPrice", item.getUnitPrice());
                    itemMap.put("amount", item.getAmount());
                    return itemMap;
                })
                .toList();

        Map<String, Object> invoiceMap = new LinkedHashMap<>();
        invoiceMap.put("id", invoice.getId());
        invoiceMap.put("invoiceNumber", invoice.getInvoiceNumber());
        invoiceMap.put("patientId", invoice.getPatient().getId());
        invoiceMap.put("patientName", invoice.getPatient().getFirstName() + " " + invoice.getPatient().getLastName());
        invoiceMap.put("status", invoice.getStatus());
        invoiceMap.put("currency", invoice.getCurrency());
        invoiceMap.put("subtotal", invoice.getSubtotal());
        invoiceMap.put("tax", invoice.getTax());
        invoiceMap.put("total", invoice.getTotal());
        invoiceMap.put("notes", invoice.getNotes() == null ? "" : invoice.getNotes());
        invoiceMap.put("draftAt", invoice.getDraftAt());
        invoiceMap.put("sentAt", invoice.getSentAt());
        invoiceMap.put("items", items);
        return invoiceMap;
    }
}
