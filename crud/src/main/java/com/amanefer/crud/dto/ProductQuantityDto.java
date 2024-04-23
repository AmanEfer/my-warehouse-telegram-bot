package com.amanefer.crud.dto;

import lombok.Data;

@Data
public class ProductQuantityDto {

    private Long id;
    private StockDto stock;
    private Integer quantity;
}
