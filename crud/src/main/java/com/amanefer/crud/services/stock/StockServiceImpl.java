package com.amanefer.crud.services.stock;

import com.amanefer.crud.dto.StockDto;
import com.amanefer.crud.mappers.StockMapper;
import com.amanefer.crud.models.Stock;
import com.amanefer.crud.repositories.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private static final String ID_NOT_FOUND_MESSAGE = "Stock with id %d not found";
    private static final String NAME_NOT_FOUND_MESSAGE = "Stock with name '%s' not found";

    private final StockRepository stockRepository;
    private final StockMapper stockMapper;

    @Override
    public List<StockDto> getAllStocks() {

        return stockRepository.findAll().stream()
                .filter(stock -> stock.getDeletedAt() == null)
                .map(stockMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public StockDto getStockById(Long id) {

        Stock stock = stockRepository.findById(id)
                .filter(stk -> stk.getDeletedAt() == null)
                .orElseThrow(() -> new IllegalArgumentException(String.format(ID_NOT_FOUND_MESSAGE, id)));

        return stockMapper.toDto(stock);
    }

    @Override
    public StockDto getStockByName(String name) {

        Stock stock = stockRepository.findByStockName(name)
                .orElseThrow(() -> new IllegalArgumentException(String.format(NAME_NOT_FOUND_MESSAGE, name)));

        return stockMapper.toDto(stock);
    }

    @Override
    @Transactional
    public StockDto saveStock(Stock stock) {

        stock.setCreatedAt(LocalDateTime.now());

        return stockMapper.toDto(stockRepository.save(stock));
    }

    @Override
    @Transactional
    public StockDto updateStock(Stock stock) {

        stock.setUpdatedAt(LocalDateTime.now());

        return stockMapper.toDto(stockRepository.save(stock));
    }

    @Override
    @Transactional
    public void deleteStock(Long id) {

        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format(ID_NOT_FOUND_MESSAGE, id)));

        stock.setDeletedAt(LocalDateTime.now());

        stockRepository.save(stock);
    }
}
