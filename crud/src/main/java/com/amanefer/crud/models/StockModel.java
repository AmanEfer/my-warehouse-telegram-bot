package com.amanefer.crud.models;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StockModel {

    private Long id;
    private String stockName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private boolean archived;
}
