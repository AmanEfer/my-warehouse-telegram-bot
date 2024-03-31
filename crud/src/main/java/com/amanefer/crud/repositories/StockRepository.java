package com.amanefer.crud.repositories;

import com.amanefer.crud.models.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByStockName(String name);
}
