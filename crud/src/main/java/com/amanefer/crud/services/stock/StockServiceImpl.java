package com.amanefer.crud.services.stock;

import com.amanefer.crud.dto.StockDto;
import com.amanefer.crud.mappers.StockMapper;
import com.amanefer.crud.entities.Stock;
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

    private static final String STOCK_ID_NOT_FOUND_MESSAGE = "Stock with ID '%d' not found";
    private static final String STOCK_NAME_NOT_FOUND_MESSAGE = "Stock with name '%s' not found";

    private final StockRepository stockRepository;
    private final StockMapper stockMapper;


    @Override
    public List<StockDto> getAllStocks() {

        return stockRepository.findAllByDeletedAtIsNull().stream()
                .map(stockMapper::fromEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public StockDto getStockById(Long id) {

        Stock stock = stockRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format(STOCK_ID_NOT_FOUND_MESSAGE, id)));

        return stockMapper.fromEntityToDto(stock);
    }

    @Override
    public StockDto getStockByName(String name) {

        Stock stock = stockRepository.findByStockNameAndDeletedAtIsNull(name)
                .orElseThrow(() -> new IllegalArgumentException(String.format(STOCK_NAME_NOT_FOUND_MESSAGE, name)));

        return stockMapper.fromEntityToDto(stock);
    }

    @Override
    @Transactional
    public StockDto saveStock(StockDto stockDto) {

        String stockName = stockDto.getStockName();

        stockRepository.findByStockName(stockName)
                .ifPresentOrElse(
                        existsStock -> {
                            if (existsStock.getDeletedAt() != null) {
                                existsStock.setDeletedAt(null);
                                existsStock.setUpdatedAt(LocalDateTime.now());
                            }
                        },

                        () -> {
                            Stock savedStock = stockMapper.fromDtoToEntity(stockDto);
                            savedStock.setCreatedAt(LocalDateTime.now());

                            stockRepository.save(savedStock);
                        }
                );

        return stockMapper.fromEntityToDto(stockRepository.findByStockName(stockName)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format(STOCK_NAME_NOT_FOUND_MESSAGE, stockName))));
    }

    @Override
    @Transactional
    public List<StockDto> saveAllStocks(List<StockDto> stockDtoList) {

        return stockDtoList.stream()
                .map(this::saveStock)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StockDto updateStock(Long id, StockDto stockDto) {

        Stock stock = stockRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format(STOCK_ID_NOT_FOUND_MESSAGE, id)));

        Stock updatedStock = stockMapper.fromDtoToEntity(stockDto);

        updatedStock.setId(id);
        updatedStock.setCreatedAt(stock.getCreatedAt());
        updatedStock.setUpdatedAt(LocalDateTime.now());

        return stockMapper.fromEntityToDto(stockRepository.save(updatedStock));
    }

    @Override
    @Transactional
    public void softDeleteStock(Long id) {

        Stock stock = stockRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format(STOCK_ID_NOT_FOUND_MESSAGE, id)));

        stock.setDeletedAt(LocalDateTime.now());

        stockRepository.save(stock);
    }

    @Override
    @Transactional
    public void hardDeleteStock(Long id) {

        stockRepository.deleteById(id);
    }
}
