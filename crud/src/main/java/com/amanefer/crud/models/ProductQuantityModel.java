package com.amanefer.crud.models;

import com.amanefer.crud.entities.Stock;
import lombok.Data;

@Data
public class ProductQuantityModel {

    private Long id;
    private Stock stock;
    private Integer quantity;
}
