package com.amanefer.crud.mappers;

import com.amanefer.crud.dto.ProductDto;
import com.amanefer.crud.models.Product;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", uses = ProductQuantityMapper.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ProductMapper extends BaseMapper<Product, ProductDto> {
    @Override
    ProductDto toDto(Product product);

    @Override
    Product toEntity(ProductDto productDto);

    List<ProductDto> toDtoList(List<Product> productList);

    List<Product> toEntityList(List<ProductDto> productDtoList);
}
