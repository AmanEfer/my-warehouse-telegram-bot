package com.amanefer.crud.controllers;

import com.amanefer.crud.dto.ProductDto;
import com.amanefer.crud.services.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/crud/products")
@RequiredArgsConstructor
public class ProductController {

    private static final String DELETE_PRODUCT_MESSAGE = "Product with ID '%s' was deleted";
    private final ProductService productService;


    @PostMapping("/all")
    @ResponseStatus(HttpStatus.CREATED)
    public List<ProductDto> saveReceivedProducts(@RequestParam("invoiceNumber") Long invoiceNumber,
                                                 @RequestParam("stockName") String stockName,
                                                 @RequestBody List<ProductDto> productDtoList) {

        log.info("The goods arrived with the invoice No.{}", invoiceNumber);

        return productService.saveAllProducts(stockName, productDtoList);
    }

    @GetMapping("/all")
    public List<ProductDto> getAllProducts() {

        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable("id") String article) {

        return productService.getProductByArticle(article);
    }

    @GetMapping
    public ProductDto getProductByName(@RequestParam("productName") String name) {

        return productService.getProductByTitle(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto saveProduct(@RequestBody ProductDto productDto) {

        return productService.saveProductAsDto(productDto);
    }

    @PatchMapping("/{id}")
    public ProductDto updateProduct(@PathVariable("id") String id, @RequestBody ProductDto productDto) {

        return productService.updateProduct(id, productDto);
    }

    @DeleteMapping("/{id}")
    public String softDeleteProduct(@PathVariable("id") String id) {

        productService.softDeleteProductByArticle(id);

        return String.format(DELETE_PRODUCT_MESSAGE, id);
    }

    @DeleteMapping("/hard/{id}")
    public String hardDeleteProduct(@PathVariable("id") String id) {

        productService.hardDeleteProductByArticle(id);

        return String.format(DELETE_PRODUCT_MESSAGE, id);
    }

}
