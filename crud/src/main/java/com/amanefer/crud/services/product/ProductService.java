package com.amanefer.crud.services.product;

import com.amanefer.crud.dto.ProductDto;

import java.util.List;

public interface ProductService {

    List<ProductDto> getAllProducts();

    ProductDto getProductById(String id);

    ProductDto getProductByName(String name);

    ProductDto saveProduct(ProductDto productDto);

    List<ProductDto> saveAllProducts(Long invoiceNumber, String stockName, List<ProductDto> productDtoList);

    ProductDto updateProduct(String id, ProductDto productDto);

    void deleteProduct(String id);
}
