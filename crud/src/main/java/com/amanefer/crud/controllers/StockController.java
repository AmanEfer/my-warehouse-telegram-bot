package com.amanefer.crud.controllers;

import com.amanefer.crud.dto.StockDto;
import com.amanefer.crud.mappers.StockMapper;
import com.amanefer.crud.services.stock.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/crud/stocks")
@RequiredArgsConstructor
public class StockController {

    public static final String DELETE_MESSAGE = "Stock with ID %d was deleted";

    private final StockService stockService;
    private final StockMapper stockMapper;

    @GetMapping("/all")
    public List<StockDto> getAllStocks() {

        return stockService.getAllStocks();
    }

    @GetMapping("/{id}")
    public StockDto getStockById(@PathVariable("id") Long id) {

        return stockService.getStockById(id);
    }

    @GetMapping
    public StockDto getStockByName(@RequestParam("stockName") String name) {

        return stockService.getStockByName(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StockDto saveStock(@RequestBody StockDto stockDto) {

        return stockService.saveStock(stockMapper.toEntity(stockDto));
    }

    @PatchMapping
    public StockDto updateStock(@RequestBody StockDto stockDto) {

        return stockService.updateStock(stockMapper.toEntity(stockDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStock(@PathVariable("id") Long id) {

        stockService.deleteStock(id);

        return new ResponseEntity<>(String.format(DELETE_MESSAGE, id), HttpStatus.OK);
    }
}
