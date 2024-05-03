package com.amanefer.crud.mappers;

import com.amanefer.crud.dto.ProductQuantityDto;
import com.amanefer.crud.entities.ProductQuantity;
import com.amanefer.crud.models.ProductQuantityModel;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = StockMapper.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ProductQuantityMapper extends BaseMapper<ProductQuantityDto, ProductQuantityModel, ProductQuantity> {

}
