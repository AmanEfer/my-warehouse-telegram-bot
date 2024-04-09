package com.amanefer.crud.services.product;

import com.amanefer.crud.dto.ProductDto;
import com.amanefer.crud.models.ProductModel;

import java.util.List;

public interface ProductService {

    List<ProductDto> getAllProducts();

    ProductDto getProductByArticle(String article);

    ProductDto getProductByTitle(String title);

    List<ProductDto> saveAllProducts(String stockName, List<ProductDto> productDtoList);

    ProductModel saveProductAsModel(ProductDto productDto);

    ProductDto saveProductAsDto(ProductDto productDto);

    ProductDto updateProduct(String id, ProductDto productDto);

    void softDeleteProductByArticle(String id);

    void hardDeleteProductByArticle(String id);

}
