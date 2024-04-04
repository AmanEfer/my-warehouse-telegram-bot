package com.amanefer.crud.services.product;

import com.amanefer.crud.dto.ProductDto;
import com.amanefer.crud.mappers.ProductMapper;
import com.amanefer.crud.models.Product;
import com.amanefer.crud.models.ProductQuantity;
import com.amanefer.crud.models.Stock;
import com.amanefer.crud.repositories.ProductRepository;
import com.amanefer.crud.repositories.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final String PRODUCT_ID_NOT_FOUND_MESSAGE = "Product with ID '%s' not found";
    private static final String PRODUCT_NAME_NOT_FOUND_MESSAGE = "Product with title '%s' not found";
    private static final String STOCK_NAME_NOT_FOUND_MESSAGE = "Stock with name '%s' not found";

    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public List<ProductDto> saveAllProducts(String stockName, List<ProductDto> productDtoList) {

        List<Product> products = productMapper.toEntityList(productDtoList);

//        products.forEach(prd -> {
//
//            Optional<Product> maybeExistsProduct = productRepository.findById(prd.getArticle());
//
//            if (maybeExistsProduct.isPresent()) {
//                Product updatedProduct = maybeExistsProduct.get();
//
//                updatedProduct.setDeletedAt(null);
//                updatedProduct.setSaleLastPrice(prd.getSaleLastPrice());
//
//                updatedProduct.getQuantityList().stream()
//                        .filter(qp -> qp.getStock().getStockName().equals(stockName))
//                        .findFirst()
//                        .ifPresentOrElse();
//            }
//
//                    prd.setCreatedAt(LocalDateTime.now());
//
//                }
//        );

        return productMapper.toDtoList(productRepository.saveAll(products));
    }


    @Override
    public List<ProductDto> getAllProducts() {

//        Page

        return productRepository.findAllByDeletedAtIsNull().stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto getProductById(String id) {

        Product product = productRepository.findById(id)
                .filter(prd -> prd.getDeletedAt() == null)
                .orElseThrow(() -> new IllegalArgumentException(String.format(PRODUCT_ID_NOT_FOUND_MESSAGE, id)));

        return productMapper.toDto(product);
    }

    @Override
    public ProductDto getProductByName(String name) {

        Product product = productRepository.findByTitle(name)
                .filter(prd -> prd.getDeletedAt() == null)
                .orElseThrow(() -> new IllegalArgumentException(String.format(PRODUCT_NAME_NOT_FOUND_MESSAGE, name)));

        return productMapper.toDto(product);
    }

    @Override
    @Transactional
    public ProductDto saveProduct(ProductDto productDto) {

        Product product = productMapper.toEntity(productDto);

        product.setCreatedAt(LocalDateTime.now());

        return productMapper.toDto(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductDto updateProduct(String id, ProductDto productDto) {

        Product product = productRepository.findById(id)
                .filter(prd -> prd.getDeletedAt() == null)
                .orElseThrow(() -> new IllegalArgumentException(String.format(PRODUCT_ID_NOT_FOUND_MESSAGE, id)));

        Product updatedProduct = productMapper.toEntity(productDto);

        updatedProduct.setArticle(id);
        updatedProduct.setCreatedAt(product.getCreatedAt());
        updatedProduct.setUpdatedAt(LocalDateTime.now());

        return productMapper.toDto(productRepository.save(updatedProduct));
    }

    @Override
    @Transactional
    public void deleteProduct(String id) {

        Product product = productRepository.findById(id)
                .filter(prd -> prd.getDeletedAt() == null)
                .orElseThrow(() -> new IllegalArgumentException(String.format(PRODUCT_ID_NOT_FOUND_MESSAGE, id)));

        product.setDeletedAt(LocalDateTime.now());

        productRepository.save(product);
    }

}
