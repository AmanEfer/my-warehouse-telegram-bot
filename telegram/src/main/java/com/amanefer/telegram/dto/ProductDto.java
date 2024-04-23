package com.amanefer.telegram.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private String article;
    private String title;
    private List<ProductQuantityDto> quantityList;
    private BigDecimal purchaseLastPrice;
    private BigDecimal saleLastPrice;
    private Integer commonQuantity;
    private BigDecimal gain;
    private String fromStock;
    private String toStock;


    public ProductDto(String article) {
        this.article = article;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDto that = (ProductDto) o;
        return Objects.equals(article, that.article);
    }

    @Override
    public int hashCode() {
        return Objects.hash(article);
    }
}
