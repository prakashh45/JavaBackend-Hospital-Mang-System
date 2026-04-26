package com.example.demo.dto.billing;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceRequest {
    private Long patientId;
    private String invoiceNumber;
    private String currency;
    private Double tax;
    private String notes;
}
