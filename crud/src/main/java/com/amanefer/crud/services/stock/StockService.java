package com.amanefer.crud.services.stock;

import com.amanefer.crud.dto.StockDto;
import com.amanefer.crud.models.Stock;

import java.util.List;

public interface StockService {
    List<StockDto> getAllStocks();

    StockDto getStockById(Long id);

    StockDto getStockByName(String name);

    StockDto saveStock(Stock stock);

    StockDto updateStock(Stock stock);

    void deleteStock(Long id);
}
