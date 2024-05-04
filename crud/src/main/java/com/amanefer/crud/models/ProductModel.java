package com.amanefer.crud.models;

import com.amanefer.crud.entities.ProductQuantity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductModel {

    private String article;
    private String title;
    private List<ProductQuantity> quantityList;
    private BigDecimal purchaseLastPrice;
    private BigDecimal saleLastPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private boolean archived;

}
