package com.amanefer.telegram.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductQuantityDto {

    private StockDto stock;
    private Integer quantity;
}
