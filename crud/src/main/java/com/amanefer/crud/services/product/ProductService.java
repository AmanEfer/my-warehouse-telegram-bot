package com.amanefer.crud.services.product;

import com.amanefer.crud.dto.ProductDto;
import com.amanefer.crud.models.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ProductService {

    List<ProductDto> getAllProducts();

    Page<ProductDto> getPageOfAllProducts(PageRequest pageRequest);

    Page<ProductDto> getPageOfAllProductsByTitle(String title, PageRequest pageRequest);

    ProductDto getProductByArticle(String article);

    ProductDto getProductByTitle(String title);

    List<ProductDto> saveAllProducts(String stockName, List<ProductDto> productDtoList);

    List<ProductDto> saleProducts(String stockName, List<ProductDto> productDtoList);

    ProductModel saveProductAsModel(ProductDto productDto);

    ProductDto saveProductAsDto(ProductDto productDto);

    ProductDto updateProduct(String id, ProductDto productDto);

    void softDeleteProductByArticle(String id);

    void hardDeleteProductByArticle(String id);

    List<ProductDto> moveProducts(String stockNameFrom, String stockNameTo, List<ProductDto> productDtoList);
}
