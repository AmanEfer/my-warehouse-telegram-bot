package com.amanefer.crud.services.product;

import com.amanefer.crud.dto.ProductDto;
import com.amanefer.crud.entities.Product;
import com.amanefer.crud.mappers.ProductMapper;
import com.amanefer.crud.repositories.ProductRepository;
import com.amanefer.crud.repositories.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final String PRODUCT_ID_NOT_FOUND_MESSAGE = "Product with ID '%s' not found";
    private static final String PRODUCT_NAME_NOT_FOUND_MESSAGE = "Product with title '%s' not found";
    private static final String STOCK_NAME_NOT_FOUND_MESSAGE = "Stock with name '%s' not found";
    public static final String QUANTITY_NOT_SPECIFIED_MESSAGE = "The quantity of the product is not specified";
    private static final String PRODUCT_ALREADY_EXISTS_MESSAGE = "Product already exists";

    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final ProductMapper productMapper;


    @Override
    @Transactional
    public List<ProductDto> saveAllProducts(String stockName, List<ProductDto> productDtoList) {

        // TODO: 07.04.2024 сделать реализацию
        return Collections.emptyList();
    }


    @Override
    public List<ProductDto> getAllProducts() {

        return productRepository.findAllByDeletedAtIsNull().stream()
                .map(productMapper::fromEntityToModel)
                .map(productMapper::fromModelToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto getProductByArticle(String article) {

        Product product = productRepository.findByArticleAndDeletedAtIsNull(article)
                .orElseThrow(() -> new IllegalArgumentException(String.format(PRODUCT_ID_NOT_FOUND_MESSAGE, article)));

        return productMapper.fromModelToDto(productMapper.fromEntityToModel(product));
    }

    @Override
    public ProductDto getProductByTitle(String title) {

        Product product = productRepository.findByTitleAndDeletedAtIsNull(title)
                .orElseThrow(() -> new IllegalArgumentException(String.format(PRODUCT_NAME_NOT_FOUND_MESSAGE, title)));

        return productMapper.fromModelToDto(productMapper.fromEntityToModel(product));
    }

    @Override
    @Transactional
    public ProductDto saveProduct(ProductDto productDto) {

        productRepository.findByTitle(productDto.getTitle()).ifPresentOrElse(
                existsProduct -> {
                    if (existsProduct.getDeletedAt() == null) {
                        throw new IllegalArgumentException(PRODUCT_ALREADY_EXISTS_MESSAGE);
                    } else {
                        existsProduct.setDeletedAt(null);
                    }
                },
                () -> {
                    Product savedProduct = productMapper.fromModelToEntity(productMapper.fromDtoToModel(productDto));
                    savedProduct.setCreatedAt(LocalDateTime.now());
                    productRepository.save(savedProduct);
                }
        );

        return productMapper.fromModelToDto(productMapper.fromEntityToModel(
                productRepository.findByTitle(productDto.getTitle()).get())
        );
    }

    @Override
    @Transactional
    public ProductDto updateProduct(String article, ProductDto productDto) {

        Product product = productRepository.findByArticleAndDeletedAtIsNull(article)
                .orElseThrow(() -> new IllegalArgumentException(String.format(PRODUCT_ID_NOT_FOUND_MESSAGE, article)));

        Product updatedProduct = productMapper.fromModelToEntity(productMapper.fromDtoToModel(productDto));

        updatedProduct.setArticle(article);
        updatedProduct.setCreatedAt(product.getCreatedAt());
        updatedProduct.setUpdatedAt(LocalDateTime.now());

        return productMapper.fromModelToDto(productMapper.fromEntityToModel(productRepository.save(updatedProduct)));
    }

    @Override
    @Transactional
    public void deleteProduct(String article) {

        Product product = productRepository.findByArticleAndDeletedAtIsNull(article)
                .orElseThrow(() -> new IllegalArgumentException(String.format(PRODUCT_ID_NOT_FOUND_MESSAGE, article)));

        product.setDeletedAt(LocalDateTime.now());

        productRepository.save(product);
    }

}
