package com.amanefer.telegram.commands.product;

import com.amanefer.telegram.cache.UserStateCache;
import com.amanefer.telegram.dto.ProductDto;
import com.amanefer.telegram.dto.ProductQuantityDto;
import com.amanefer.telegram.dto.StockDto;
import com.amanefer.telegram.services.RestToCrud;
import com.amanefer.telegram.util.Button;
import com.amanefer.telegram.util.UpdateTransferData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class GettingAllProductsCommandTest {

    @Mock
    private RestToCrud rest;

    @Mock
    private UserStateCache userStateCache;

    @InjectMocks
    private GettingAllProductsCommand command;
    private StockDto stock1;
    private StockDto stock2;


    @BeforeEach
    void init() {

        stock1 = StockDto.builder()
                .stockName("stock1")
                .build();

        stock2 = StockDto.builder()
                .stockName("stock2")
                .build();
    }

    @Test
    void gettingAllProductsCommand_supportTest() {

        assertTrue(command.support(Button.GET_ALL_PRODUCTS_BUTTON.toString()));
    }

    @Test
    void gettingAllProductsCommand_processTest_checkReturnedMessageText() {

        List<ProductDto> products = List.of(
                ProductDto.builder()
                        .article("p1")
                        .title("product1")
                        .purchaseLastPrice(BigDecimal.valueOf(10.00))
                        .saleLastPrice(BigDecimal.valueOf(20.00))
                        .quantityList(List.of(
                                ProductQuantityDto.builder()
                                        .stock(stock1)
                                        .quantity(50)
                                        .build(),
                                ProductQuantityDto.builder()
                                        .stock(stock2)
                                        .quantity(30)
                                        .build()))
                        .build(),

                ProductDto.builder()
                        .article("p2")
                        .title("product2")
                        .purchaseLastPrice(BigDecimal.valueOf(15.00))
                        .saleLastPrice(BigDecimal.valueOf(40.00))
                        .quantityList(List.of(
                                ProductQuantityDto.builder()
                                        .stock(stock1)
                                        .quantity(100)
                                        .build()
                        ))
                        .build(),

                ProductDto.builder()
                        .article("p3")
                        .title("product3")
                        .build()
        );

        Mockito.when(rest.getAllProducts()).thenReturn(products);

        UpdateTransferData data = UpdateTransferData.builder()
                .chatId(111111)
                .userId(222222)
                .build();

        PartialBotApiMethod<Message> actual = command.process(data);

        String expectedText = """
                1) Article: p1
                Title: product1
                Purchase last price: 10,00
                Sale last price: 20,00
                There are 50 items in the 'stock1'
                There are 30 items in the 'stock2'
                Common quantity: 80
                                
                2) Article: p2
                Title: product2
                Purchase last price: 15,00
                Sale last price: 40,00
                There are 100 items in the 'stock1'
                Common quantity: 100
                                
                3) Article: p3
                Title: product3
                Common quantity: 0
                                
                """;

        assertAll(() -> {
            assertTrue(actual instanceof SendMessage);
            assertEquals(expectedText, ((SendMessage) actual).getText());
        });
    }
}
