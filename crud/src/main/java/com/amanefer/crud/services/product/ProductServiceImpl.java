package com.amanefer.crud.services.product;

import com.amanefer.crud.dto.ProductDto;
import com.amanefer.crud.entities.Product;
import com.amanefer.crud.entities.ProductQuantity;
import com.amanefer.crud.mappers.ProductMapper;
import com.amanefer.crud.mappers.StockMapper;
import com.amanefer.crud.models.ProductModel;
import com.amanefer.crud.models.StockModel;
import com.amanefer.crud.repositories.ProductRepository;
import com.amanefer.crud.repositories.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private final StockMapper stockMapper;


    @Override
    @Transactional
    public List<ProductDto> saveAllProducts(String stockName, List<ProductDto> productDtoList) {

        StockModel stockModel = stockMapper.fromEntityToModel(
                stockRepository.findByStockNameAndDeletedAtIsNull(stockName).orElseThrow(() ->
                        new IllegalArgumentException(String.format(STOCK_NAME_NOT_FOUND_MESSAGE, stockName))));

        List<ProductModel> models = new ArrayList<>();

        for (ProductDto dto : productDtoList) {

            ProductModel model = saveProductAsModel(dto);

            if (model.getQuantityList() == null) {
                model.setQuantityList(new ArrayList<>());
            }

            model.getQuantityList().stream()
                    .filter(pq -> pq.getStock().getStockName().equals(stockName))
                    .findFirst()
                    .ifPresentOrElse(
                            q -> q.setQuantity(q.getQuantity() + dto.getCommonQuantity()),

                            () -> model.getQuantityList().add(new ProductQuantity(
                                    stockMapper.fromModelToEntity(stockModel), dto.getCommonQuantity()))
                    );
            models.add(model);
        }
        List<Product> products = productRepository.saveAll(productMapper.fromModelToEntityList(models));

        return productMapper.fromModelToDtoList(productMapper.fromEntityToModelList(products));
    }

    @Override
    @Transactional
    public ProductModel saveProductAsModel(ProductDto productDto) {

        productRepository.findByTitle(productDto.getTitle()).ifPresentOrElse(
                existsProduct -> {
                    if (existsProduct.getDeletedAt() != null) {
                        existsProduct.setDeletedAt(null);
                        existsProduct.setUpdatedAt(LocalDateTime.now());
                    }
                    existsProduct.setPurchaseLastPrice(productDto.getPurchaseLastPrice());
                },

                () -> {
                    Product savedProduct = productMapper.fromModelToEntity(productMapper.fromDtoToModel(productDto));
                    savedProduct.setCreatedAt(LocalDateTime.now());
                    productRepository.save(savedProduct);
                }
        );

        return productMapper.fromEntityToModel(productRepository.findByTitle(productDto.getTitle()).get());
    }

    @Override
    @Transactional
    public ProductDto saveProductAsDto(ProductDto productDto) {

        return productMapper.fromModelToDto(saveProductAsModel(productDto));
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
    public void softDeleteProductByArticle(String article) {

        Product product = productRepository.findByArticleAndDeletedAtIsNull(article)
                .orElseThrow(() -> new IllegalArgumentException(String.format(PRODUCT_ID_NOT_FOUND_MESSAGE, article)));

        product.setDeletedAt(LocalDateTime.now());
        product.getQuantityList().clear();

        productRepository.save(product);
    }

    @Override
    @Transactional
    public void hardDeleteProductByArticle(String article) {

        productRepository.deleteById(article);
    }
}
