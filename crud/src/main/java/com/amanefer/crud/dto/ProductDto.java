package com.amanefer.crud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private String article;
    private String title;
    private BigDecimal purchaseLastPrice;
    private BigDecimal saleLastPrice;
    private List<ProductQuantityDto> quantityList;
    private Integer commonQuantity;
    private BigDecimal gain;
}
