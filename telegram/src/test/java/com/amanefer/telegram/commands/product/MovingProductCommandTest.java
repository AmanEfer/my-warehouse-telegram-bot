package com.amanefer.telegram.commands.product;

import com.amanefer.telegram.cache.ProductListCache;
import com.amanefer.telegram.cache.ProductStateCache;
import com.amanefer.telegram.cache.ProductTransferDataCache;
import com.amanefer.telegram.cache.UserStateCache;
import com.amanefer.telegram.dto.ProductDto;
import com.amanefer.telegram.dto.ProductQuantityDto;
import com.amanefer.telegram.dto.StockDto;
import com.amanefer.telegram.services.RestToCrud;
import com.amanefer.telegram.util.UserState;
import com.amanefer.telegram.util.Button;
import com.amanefer.telegram.util.ProductTransferData;
import com.amanefer.telegram.util.UpdateTransferData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(MockitoExtension.class)
class MovingProductCommandTest {

    public static final String INPUT_INVOICE_NUMBER_TEXT = "Please, input the invoice number";
    public static final String INPUT_STOCK_FROM_TEXT = "Now input warehouse's title from which the products pick up";
    public static final String INPUT_STOCK_TO_TEXT = "Input warehouse's title to which we deliver products";
    public static final String INPUT_PRODUCT_QUANTITY_TEXT = "Input the quantity of the item you want to move";
    public static final String INPUT_PRODUCT_ARTICLE_TEXT = "Input product's article you want to move";
    public static final String ADD_ANOTHER_PRODUCT_TEXT = "Do you want to add one more product to list to move?";
    public static final String PRODUCTS_MOVE_FAIL_TEXT = "Products weren't moved, try again";
    public static final String INVOICE_NUMBER = "№A555";
    public static final String STOCK_FROM = "stock1";
    public static final String STOCK_TO = "stock2";

    @Mock
    private RestToCrud rest;

    @Mock
    private UserStateCache userStateCache;

    @Mock
    private ProductStateCache productStateCache;

    @Mock
    private ProductListCache productListCache;

    @Mock
    private ProductTransferDataCache productTransferDataCache;

    @InjectMocks
    private MovingProductCommand command;

    private UpdateTransferData updateTransferData;


    @BeforeEach
    void init() {

        updateTransferData = UpdateTransferData.builder()
                .chatId(111111)
                .userId(222222)
                .build();
    }

    @Test
    void movingProductCommand_supportTest() {

        assertTrue(command.support(Button.MOVE_PRODUCTS_BUTTON.toString()));
    }

    @Test
    void movingProductCommand_processTest_checkReturnedMessageTextIfUserStateIsPrimary() {

        Mockito.when(userStateCache.getFromCache(updateTransferData.getUserId())).thenReturn(UserState.PRIMARY);

        PartialBotApiMethod<Message> actual = command.process(updateTransferData);

        assertAll(() -> {
            assertTrue(actual instanceof SendMessage);
            assertEquals(INPUT_INVOICE_NUMBER_TEXT, ((SendMessage) actual).getText());
        });
    }

    @Test
    void movingProductCommand_processTest_checkReturnedMessageTextIfUserStateIsMoveProductInputInvoiceNumber() {

        Mockito.when(userStateCache.getFromCache(updateTransferData.getUserId()))
                .thenReturn(UserState.MOVE_PRODUCT_INPUT_INVOICE_NUMBER);

        PartialBotApiMethod<Message> actual = command.process(updateTransferData);

        assertAll(() -> {
            assertTrue(actual instanceof SendMessage);
            assertEquals(INPUT_STOCK_FROM_TEXT, ((SendMessage) actual).getText());
        });
    }

    @Test
    void movingProductCommand_processTest_checkReturnedMessageTextIfUserStateIsMoveProductInputStockFrom() {

        Mockito.when(userStateCache.getFromCache(updateTransferData.getUserId()))
                .thenReturn(UserState.MOVE_PRODUCT_INPUT_STOCK_FROM);

        updateTransferData.setDataForDto(STOCK_FROM);

        ProductTransferData productTransferData = new ProductTransferData(INVOICE_NUMBER);

        Mockito.when(productTransferDataCache.getFromCache(updateTransferData.getUserId()))
                .thenReturn(productTransferData);

        PartialBotApiMethod<Message> actual = command.process(updateTransferData);

        assertAll(() -> {
            assertTrue(actual instanceof SendMessage);
            assertEquals(INPUT_STOCK_TO_TEXT, ((SendMessage) actual).getText());
        });
    }

    @Test
    void movingProductCommand_processTest_checkReturnedMessageTextIfUserStateIsMoveProductInputStockTo() {

        Mockito.when(userStateCache.getFromCache(updateTransferData.getUserId()))
                .thenReturn(UserState.MOVE_PRODUCT_INPUT_STOCK_TO);

        updateTransferData.setDataForDto(STOCK_TO);

        ProductTransferData productTransferData = ProductTransferData.builder()
                .invoiceNumber(INVOICE_NUMBER)
                .stockNameFrom(STOCK_FROM)
                .build();

        Mockito.when(productTransferDataCache.getFromCache(updateTransferData.getUserId()))
                .thenReturn(productTransferData);

        PartialBotApiMethod<Message> actual = command.process(updateTransferData);

        assertAll(() -> {
            assertTrue(actual instanceof SendMessage);
            assertEquals(INPUT_PRODUCT_ARTICLE_TEXT, ((SendMessage) actual).getText());
        });
    }

    @Test
    void movingProductCommand_processTest_checkReturnedMessageTextIfUserStateIsMoveProductInputProductArticle() {

        Mockito.when(userStateCache.getFromCache(updateTransferData.getUserId()))
                .thenReturn(UserState.MOVE_PRODUCT_INPUT_PRODUCT_ARTICLE);

        PartialBotApiMethod<Message> actual = command.process(updateTransferData);

        assertAll(() -> {
            assertTrue(actual instanceof SendMessage);
            assertEquals(INPUT_PRODUCT_QUANTITY_TEXT, ((SendMessage) actual).getText());
        });
    }

    @Test
    void movingProductCommand_processTest_checkReturnedMessageTextIfUserStateIsMoveProductInputProductQuantity() {

        Mockito.when(userStateCache.getFromCache(updateTransferData.getUserId()))
                .thenReturn(UserState.MOVE_PRODUCT_INPUT_PRODUCT_QUANTITY);

        updateTransferData.setDataForDto("100");

        ProductDto product = new ProductDto("p1");

        Mockito.when(productStateCache.getFromCache(updateTransferData.getUserId()))
                .thenReturn(product);

        List<ProductDto> products = new ArrayList<>();

        Mockito.when(productListCache.getFromCache(updateTransferData.getUserId()))
                .thenReturn(products);

        InlineKeyboardButton yesButton = InlineKeyboardButton.builder()
                .text(Button.YES_MOVE_PRODUCT_BUTTON.getKeyboardName())
                .callbackData(Button.YES_MOVE_PRODUCT_BUTTON.toString())
                .build();

        InlineKeyboardButton noButton = InlineKeyboardButton.builder()
                .text(Button.NO_MOVE_PRODUCT_BUTTON.getKeyboardName())
                .callbackData(Button.NO_MOVE_PRODUCT_BUTTON.toString())
                .build();

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(yesButton);
        row.add(noButton);

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(row);

        InlineKeyboardMarkup expectedKeyboardMarkup = new InlineKeyboardMarkup();
        expectedKeyboardMarkup.setKeyboard(keyboard);

        PartialBotApiMethod<Message> actual = command.process(updateTransferData);

        assertAll(() -> {
            assertTrue(actual instanceof SendMessage);
            assertEquals(ADD_ANOTHER_PRODUCT_TEXT, ((SendMessage) actual).getText());
            assertEquals(expectedKeyboardMarkup, ((SendMessage) actual).getReplyMarkup());
        });
    }

    @Test
    void movingProductCommand_processTest_checkReturnedMessageTextIfUserStateIsMoveProduct() {

        Mockito.when(userStateCache.getFromCache(updateTransferData.getUserId()))
                .thenReturn(UserState.MOVE_PRODUCT);

        ProductTransferData productTransferData = ProductTransferData.builder()
                .invoiceNumber(INVOICE_NUMBER)
                .stockNameFrom(STOCK_FROM)
                .stockNameTo(STOCK_TO)
                .build();

        Mockito.when(productTransferDataCache.getFromCache(updateTransferData.getUserId()))
                .thenReturn(productTransferData);

        List<ProductDto> products = List.of(
                ProductDto.builder()
                        .article("p1")
                        .commonQuantity(30)
                        .build(),

                ProductDto.builder()
                        .article("p2")
                        .commonQuantity(50)
                        .build(),

                ProductDto.builder()
                        .article("p3")
                        .commonQuantity(100)
                        .build()
        );

        Mockito.when(productListCache.getFromCache(updateTransferData.getUserId()))
                .thenReturn(products);

        List<ProductDto> movedProducts = List.of(
                ProductDto.builder()
                        .article("p1")
                        .title("product1")
                        .purchaseLastPrice(BigDecimal.valueOf(10.00))
                        .saleLastPrice(BigDecimal.valueOf(20.00))
                        .quantityList(List.of(
                                ProductQuantityDto.builder()
                                        .stock(new StockDto("stock1"))
                                        .quantity(50)
                                        .build(),
                                ProductQuantityDto.builder()
                                        .stock(new StockDto("stock2"))
                                        .quantity(30)
                                        .build()
                        ))
                        .build(),

                ProductDto.builder()
                        .article("p2")
                        .title("product2")
                        .purchaseLastPrice(BigDecimal.valueOf(15.00))
                        .saleLastPrice(BigDecimal.valueOf(40.00))
                        .quantityList(List.of(
                                ProductQuantityDto.builder()
                                        .stock(new StockDto("stock2"))
                                        .quantity(100)
                                        .build()
                        ))
                        .build(),

                ProductDto.builder()
                        .article("p3")
                        .title("product3")
                        .build()
        );

        Mockito.when(rest.moveProducts(
                        productTransferData.getInvoiceNumber(),
                        productTransferData.getStockNameFrom(),
                        productTransferData.getStockNameTo(),
                        products))
                .thenReturn(movedProducts);

        PartialBotApiMethod<Message> actual = command.process(updateTransferData);

        String expectedText = """
                1) Article: p1
                Title: product1
                There are 50 items in the 'stock1'
                There are 30 items in the 'stock2'
                Common quantity: 80
                                
                2) Article: p2
                Title: product2
                There are 100 items in the 'stock2'
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

    @ParameterizedTest
    @MethodSource("getUserStates")
    void movingProductCommand_processTest_checkReturnedMessageTextIfUserStateIsOther(UserState state) {

        Mockito.when(userStateCache.getFromCache(updateTransferData.getUserId()))
                .thenReturn(state);

        PartialBotApiMethod<Message> actual = command.process(updateTransferData);

        assertAll(() -> {
            assertTrue(actual instanceof SendMessage);
            assertEquals(PRODUCTS_MOVE_FAIL_TEXT, ((SendMessage) actual).getText());
        });
    }

    private static Stream<Arguments> getUserStates() {

        return Stream.of(
                Arguments.of(UserState.CREATE_STOCK),
                Arguments.of(UserState.SAVE_PRODUCT_INPUT_PRODUCT_TITLE),
                Arguments.of(UserState.SAVE_PRODUCT)
        );
    }
}
