package com.amanefer.telegram.dto;

import lombok.Data;

@Data
public class ProductQuantityDto {

    private StockDto stock;
    private Integer quantity;
}
