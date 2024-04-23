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

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    public List<ProductDto> saleProducts(String stockName, List<ProductDto> productDtoList) {

        if (stockRepository.findByStockNameAndDeletedAtIsNull(stockName).isEmpty()) {
            throw new IllegalArgumentException(String.format(STOCK_NAME_NOT_FOUND_MESSAGE, stockName));
        }

        List<ProductDto> soldProducts = new ArrayList<>();

        for (ProductDto dto : productDtoList) {

            productRepository.findById(dto.getArticle()).ifPresent(
                    product -> {
                        product.getQuantityList().stream()
                                .filter(pq -> pq.getStock().getStockName().equals(stockName))
                                .findFirst()
                                .ifPresent(
                                        productQuantity -> {

                                            product.setSaleLastPrice(dto.getSaleLastPrice());
                                            product.setUpdatedAt(LocalDateTime.now());

                                            BigDecimal gain;
                                            int diff = productQuantity.getQuantity() - dto.getCommonQuantity();

                                            if (diff > 0) {
                                                gain = dto.getSaleLastPrice()
                                                        .multiply(BigDecimal.valueOf(dto.getCommonQuantity()));

                                                productQuantity.setQuantity(diff);

                                            } else {
                                                gain = dto.getSaleLastPrice()
                                                        .multiply(BigDecimal.valueOf(productQuantity.getQuantity()));

                                                product.getQuantityList().remove(productQuantity);
                                            }

                                            ProductDto productDto = productMapper.fromModelToDto(
                                                    productMapper.fromEntityToModel(product));

                                            productDto.setGain(gain.setScale(2, RoundingMode.HALF_UP));

                                            soldProducts.add(productDto);
                                        }
                                );
                    }
            );
        }
        return soldProducts;
    }

    @Override
    @Transactional
    public List<ProductDto> moveProducts(String stockNameFrom, String stockNameTo, List<ProductDto> productDtoList) {

        if (stockRepository.findByStockNameAndDeletedAtIsNull(stockNameFrom).isEmpty()) {
            throw new IllegalArgumentException(String.format(STOCK_NAME_NOT_FOUND_MESSAGE, stockNameFrom));
        }

        StockModel stockModel = stockMapper.fromEntityToModel(
                stockRepository.findByStockNameAndDeletedAtIsNull(stockNameTo).orElseThrow(
                        () -> new IllegalArgumentException(String.format(STOCK_NAME_NOT_FOUND_MESSAGE, stockNameTo))));

        List<ProductDto> movedProducts = new ArrayList<>();

        for (ProductDto dto : productDtoList) {

            productRepository.findByArticleAndDeletedAtIsNull(dto.getArticle()).ifPresent(
                    product -> {
                        product.getQuantityList().stream()
                                .filter(qp -> qp.getStock().getStockName().equals(stockNameFrom))
                                .findFirst()
                                .ifPresent(
                                        productQuantityFrom -> {
                                            ProductQuantity productQuantityTo = product.getQuantityList().stream()
                                                    .filter(q -> q.getStock().getStockName().equals(stockNameTo))
                                                    .findFirst()
                                                    .orElse(new ProductQuantity(
                                                            stockMapper.fromModelToEntity(stockModel), 0));

                                            int diff = productQuantityFrom.getQuantity() - dto.getCommonQuantity();

                                            if (diff > 0) {
                                                productQuantityFrom.setQuantity(diff);
                                                productQuantityTo.setQuantity(productQuantityTo.getQuantity() + dto.getCommonQuantity());

                                            } else {
                                                productQuantityTo.setQuantity(productQuantityTo.getQuantity() + productQuantityFrom.getQuantity());
                                                product.getQuantityList().remove(productQuantityFrom);
                                            }

                                            product.setUpdatedAt(LocalDateTime.now());
                                            product.getQuantityList().add(productQuantityTo);

                                            ProductDto productDto = productMapper.fromModelToDto(
                                                    productMapper.fromEntityToModel(product));

                                            movedProducts.add(productDto);
                                        }
                                );
                    }
            );
        }
        return movedProducts;
    }

    @Override
    @Transactional
    public ProductModel saveProductAsModel(ProductDto productDto) {

        productRepository.findById(productDto.getArticle()).ifPresentOrElse(
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

        return productMapper.fromEntityToModel(productRepository.findById(productDto.getArticle()).get());
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
