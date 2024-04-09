package com.amanefer.crud.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDto {

    private String article;
    private String title;
    private BigDecimal purchaseLastPrice;
    private BigDecimal saleLastPrice;
    private List<ProductQuantityDto> quantityList;
    private Integer commonQuantity;
    private BigDecimal gain;
}
