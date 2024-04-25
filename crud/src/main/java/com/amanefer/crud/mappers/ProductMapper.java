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

    @Override
    ProductModel fromDtoToModel(ProductDto productDto);

    @Override
    Product fromModelToEntity(ProductModel productModel);

    @Override
    ProductModel fromEntityToModel(Product product);

    @Override
    ProductDto fromModelToDto(ProductModel productModel);

    @Override
    ProductDto fromEntityToDto(Product product);

    @Override
    Product fromDtoToEntity(ProductDto productDto);

    List<Product> fromModelToEntityList(List<ProductModel> productModelList);

    List<ProductDto> fromEntityToDtoList(List<Product> productList);


    default Page<ProductDto> fromEntityToDtoPage(Page<Product> productPage) {

        return productPage.map(this::fromEntityToDto);
    }

}
