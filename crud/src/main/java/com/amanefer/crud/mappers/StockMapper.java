package com.amanefer.crud.mappers;

import com.amanefer.crud.dto.StockDto;
import com.amanefer.crud.models.Stock;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface StockMapper extends BaseMapper<Stock, StockDto> {

    @Override
    StockDto toDto(Stock stock);

    @Override
    Stock toEntity(StockDto stockDto);

    List<StockDto> toDtoList(List<Stock> stockList);

    List<Stock> toEntityList(List<StockDto> stockDtoList);
}
