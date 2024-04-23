package com.amanefer.crud.services.product;

import com.amanefer.crud.dto.ProductDto;
import com.amanefer.crud.dto.ProductQuantityDto;
import com.amanefer.crud.dto.StockDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Sql("/sql/initTest.sql")
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ProductServiceImplIT {

    private final static String STOCK_1 = "TestStock1";
    private final static String STOCK_2 = "TestStock2";
    private final static String NON_EXIST_STOCK_1 = "Non-exists stock1";
    private final static String NON_EXIST_STOCK_2 = "Non-exists stock2";

    List<ProductDto> products;

    @Autowired
    ProductServiceImpl productService;


    @BeforeEach
    void init() {

        products = new ArrayList<>(
                List.of(
                        ProductDto.builder()
                                .article("tp1")
                                .title("TestProduct1")
                                .purchaseLastPrice(BigDecimal.valueOf(10.0))
                                .commonQuantity(100)
                                .build(),

                        ProductDto.builder()
                                .article("tp2")
                                .title("TestProduct2")
                                .purchaseLastPrice(BigDecimal.valueOf(20.0))
                                .commonQuantity(100)
                                .build(),

                        ProductDto.builder()
                                .article("tp3")
                                .title("TestProduct3")
                                .purchaseLastPrice(BigDecimal.valueOf(30.0))
                                .commonQuantity(100)
                                .build()
                )
        );
    }

    @Test
    void saveAllProducts_returnSavedProducts() {

        List<ProductDto> returnedProducts = productService.saveAllProducts(STOCK_1, products);

        List<String> expectedIdList = products.stream()
                .map(ProductDto::getArticle)
                .toList();

        List<String> actualIdList = returnedProducts.stream()
                .map(ProductDto::getArticle)
                .toList();

        assertAll(() -> {
            assertEquals(3, returnedProducts.size());
            assertEquals(expectedIdList, actualIdList);
        });
    }

    @Test
    void saveAllProducts_stockNotFound() {

        assertThrows(IllegalArgumentException.class, () -> productService.saleProducts(NON_EXIST_STOCK_1, products));
    }

    @Test
    void saleProducts_returnSoldProducts() {

        productService.saveAllProducts(STOCK_1, products);

        List<ProductDto> productsForSale = new ArrayList<>(
                List.of(
                        ProductDto.builder()
                                .article("tp1")
                                .title("TestProduct1")
                                .saleLastPrice(BigDecimal.valueOf(20.00))
                                .commonQuantity(50)
                                .build(),

                        ProductDto.builder()
                                .article("tp2")
                                .title("TestProduct2")
                                .saleLastPrice(BigDecimal.valueOf(40.00))
                                .commonQuantity(100)
                                .build(),

                        ProductDto.builder()
                                .article("tp3")
                                .title("TestProduct3")
                                .saleLastPrice(BigDecimal.valueOf(60.00))
                                .commonQuantity(200)
                                .build()
                )
        );

        List<ProductDto> soldProducts = productService.saleProducts(STOCK_1, productsForSale);

        List<BigDecimal> actualGainList = soldProducts.stream()
                .map(ProductDto::getGain)
                .toList();

        List<BigDecimal> expectedGainList = List.of(
                BigDecimal.valueOf(1000.00).setScale(2),
                BigDecimal.valueOf(4000.00).setScale(2),
                BigDecimal.valueOf(6000.00).setScale(2));

        assertAll(() -> {
            assertEquals(3, soldProducts.size());
            assertEquals(expectedGainList, actualGainList);
            assertEquals(50, soldProducts.get(0).getQuantityList().get(0).getQuantity());
            assertTrue(soldProducts.get(1).getQuantityList().isEmpty());
            assertTrue(soldProducts.get(2).getQuantityList().isEmpty());
        });
    }

    @Test
    void saleProducts_stockNotFound() {

        assertThrows(IllegalArgumentException.class, () -> productService.saleProducts(NON_EXIST_STOCK_1, products));
    }

    @Test
    void moveProducts_returnMovedProducts() {

        productService.saveAllProducts(STOCK_1, products);

        List<ProductDto> productsForMove = new ArrayList<>(
                List.of(
                        ProductDto.builder()
                                .article("tp1")
                                .title("TestProduct1")
                                .commonQuantity(50)
                                .build(),

                        ProductDto.builder()
                                .article("tp2")
                                .title("TestProduct2")
                                .commonQuantity(100)
                                .build(),

                        ProductDto.builder()
                                .article("tp3")
                                .title("TestProduct3")
                                .commonQuantity(200)
                                .build()
                )
        );

        List<ProductDto> movedProducts = productService.moveProducts(STOCK_1, STOCK_2, productsForMove);

        ProductDto firstProduct = movedProducts.get(0);
        ProductDto secondProduct = movedProducts.get(1);
        ProductDto thirdProduct = movedProducts.get(2);

        List<String> firstProductStocks = firstProduct.getQuantityList().stream()
                .map(ProductQuantityDto::getStock)
                .map(StockDto::getStockName)
                .toList();

        List<String> secondProductStocks = secondProduct.getQuantityList().stream()
                .map(ProductQuantityDto::getStock)
                .map(StockDto::getStockName)
                .toList();

        List<String> thirdProductStocks = thirdProduct.getQuantityList().stream()
                .map(ProductQuantityDto::getStock)
                .map(StockDto::getStockName)
                .toList();

        assertAll(() -> {
            assertEquals(3, movedProducts.size());

            assertEquals(2, firstProduct.getQuantityList().size());
            assertEquals(STOCK_1, firstProduct.getQuantityList().get(0).getStock().getStockName());
            assertEquals(50, firstProduct.getQuantityList().get(0).getQuantity());
            assertEquals(STOCK_2, firstProduct.getQuantityList().get(1).getStock().getStockName());
            assertEquals(50, firstProduct.getQuantityList().get(1).getQuantity());
            assertTrue(firstProductStocks.contains(STOCK_1));
            assertTrue(firstProductStocks.contains(STOCK_2));

            assertEquals(1, secondProduct.getQuantityList().size());
            assertEquals(STOCK_2, secondProduct.getQuantityList().get(0).getStock().getStockName());
            assertEquals(100, secondProduct.getQuantityList().get(0).getQuantity());
            assertTrue(secondProductStocks.contains(STOCK_2));
            assertFalse(secondProductStocks.contains(STOCK_1));

            assertEquals(1, thirdProduct.getQuantityList().size());
            assertEquals(STOCK_2, thirdProduct.getQuantityList().get(0).getStock().getStockName());
            assertEquals(100, thirdProduct.getQuantityList().get(0).getQuantity());
            assertTrue(thirdProductStocks.contains(STOCK_2));
            assertFalse(thirdProductStocks.contains(STOCK_1));
        });
    }

    @Test
    void moveProducts_stockFromNotFound() {

        assertThrows(IllegalArgumentException.class,
                () -> productService.moveProducts(NON_EXIST_STOCK_1, NON_EXIST_STOCK_2, products));
    }

    @Test
    void moveProducts_stockToNotFound() {

        assertThrows(IllegalArgumentException.class,
                () -> productService.moveProducts(NON_EXIST_STOCK_1, NON_EXIST_STOCK_2, products));
    }
}
