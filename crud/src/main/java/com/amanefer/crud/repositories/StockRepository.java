package com.amanefer.crud.repositories;

import com.amanefer.crud.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByStockName(String name);

    List<Stock> findAllByDeletedAtIsNull();

    Optional<Stock> findByIdAndDeletedAtIsNull(Long id);

    Optional<Stock> findByStockNameAndDeletedAtIsNull(String name);
}
