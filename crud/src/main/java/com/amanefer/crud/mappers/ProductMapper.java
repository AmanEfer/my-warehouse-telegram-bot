package com.amanefer.crud.mappers;

import com.amanefer.crud.dto.ProductDto;
import com.amanefer.crud.entities.Product;
import com.amanefer.crud.models.ProductModel;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", uses = ProductQuantityMapper.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ProductMapper extends BaseMapper<ProductDto, ProductModel, Product> {

    List<Product> fromModelToEntityList(List<ProductModel> productModelList);

    List<ProductDto> fromEntityToDtoList(List<Product> productList);


    default Page<ProductDto> fromEntityToDtoPage(Page<Product> productPage) {

        return productPage.map(this::fromEntityToDto);
    }

}
