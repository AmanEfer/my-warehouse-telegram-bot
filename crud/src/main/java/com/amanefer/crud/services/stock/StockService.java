package com.amanefer.crud.services.stock;

import com.amanefer.crud.dto.StockDto;

import java.util.List;

public interface StockService {

    List<StockDto> getAllStocks();

    StockDto getStockById(Long id);

    StockDto getStockByName(String name);

    StockDto saveStock(StockDto stockDto);

    List<StockDto> saveAllStocks(List<StockDto> stockDtoList);

    StockDto updateStock(Long id, StockDto stockDto);

    void deleteStock(Long id);
}
