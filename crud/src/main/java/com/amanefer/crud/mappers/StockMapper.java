package com.amanefer.crud.mappers;

import com.amanefer.crud.dto.StockDto;
import com.amanefer.crud.models.Stock;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface StockMapper extends BaseMapper<Stock, StockDto> {

    @Override
    StockDto toDto(Stock stock);

    @Override
    Stock toEntity(StockDto stockDto);
}
