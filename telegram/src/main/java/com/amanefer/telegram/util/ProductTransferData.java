package com.amanefer.telegram.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
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
