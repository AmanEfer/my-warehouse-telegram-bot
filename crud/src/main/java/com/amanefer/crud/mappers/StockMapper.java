package com.amanefer.crud.mappers;

import com.amanefer.crud.dto.StockDto;
import com.amanefer.crud.entities.Stock;
import com.amanefer.crud.models.StockModel;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface StockMapper extends BaseMapper<StockDto, StockModel, Stock> {

    @Override
    StockModel fromDtoToModel(StockDto stockDto);

    @Override
    Stock fromModelToEntity(StockModel stockModel);

    @Override
    StockModel fromEntityToModel(Stock stock);

    @Override
    StockDto fromModelToDto(StockModel stockModel);

    @Override
    StockDto fromEntityToDto(Stock stock);

    @Override
    Stock fromDtoToEntity(StockDto stockDto);

}
