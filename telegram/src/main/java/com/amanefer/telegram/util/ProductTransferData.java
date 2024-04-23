package com.amanefer.telegram.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductTransferData {

    public ProductTransferData(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    private String invoiceNumber;
    private String stockNameFrom;
    private String stockNameTo;
}
