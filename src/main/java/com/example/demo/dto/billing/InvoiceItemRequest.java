package com.example.demo.dto.billing;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceItemRequest {
    private String itemName;
    private Integer quantity;
    private Double unitPrice;
}
