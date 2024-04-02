package com.amanefer.crud.mappers;

import com.amanefer.crud.dto.ProductQuantityDto;
import com.amanefer.crud.models.ProductQuantity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", uses = StockMapper.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ProductQuantityMapper extends BaseMapper<ProductQuantity, ProductQuantityDto> {

    @Override
    ProductQuantityDto toDto(ProductQuantity productQuantity);

    @Override
    ProductQuantity toEntity(ProductQuantityDto productQuantityDto);

    List<ProductQuantityDto> toDtoList(List<ProductQuantity> productQuantityList);

    List<ProductQuantity> toEntityList(List<ProductQuantityDto> productQuantityDtoList);
}
