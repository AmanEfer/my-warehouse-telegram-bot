package com.amanefer.telegram.commands.product;

import com.amanefer.telegram.cache.ProductStateCache;
import com.amanefer.telegram.cache.UserStateCache;
import com.amanefer.telegram.dto.ProductDto;
import com.amanefer.telegram.services.RestToCrud;
import com.amanefer.telegram.util.UserState;
import com.amanefer.telegram.util.Button;
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

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class SaveProductCommandTest {

    public static final String INPUT_PRODUCT_ARTICLE_TEXT = "Please, input the article of the new product";
    public static final String INPUT_PRODUCT_TITLE_TEXT = "Now input the product title";
    public static final String PRODUCT_SAVE_MESSAGE_TEXT = "Product '%s' was saved successfully!";
    public static final String PRODUCT_SAVE_FAIL_TEXT = "Product wasn't saved, try again";

    @Mock
    private RestToCrud rest;

    @Mock
    private UserStateCache userStateCache;

    @Mock
    private ProductStateCache productStateCache;

    @InjectMocks
    private SaveProductCommand command;

    private UpdateTransferData data;

    @BeforeEach
    void init() {

        data = UpdateTransferData.builder()
                .chatId(111111)
                .userId(222222)
                .build();
    }


    @Test
    void saveProductCommand_supportTest() {

        assertTrue(command.support(Button.SAVE_NEW_PRODUCT.toString()));
    }

    @Test
    void saveProductCommand_processTest_checkReturnedMessageTextIfUserStateIsPrimary() {

        Mockito.when(userStateCache.getFromCache(data.getUserId())).thenReturn(UserState.PRIMARY);

        PartialBotApiMethod<Message> actual = command.process(data);

        assertAll(() -> {
            assertTrue(actual instanceof SendMessage);
            assertEquals(INPUT_PRODUCT_ARTICLE_TEXT, ((SendMessage) actual).getText());
        });
    }

    @Test
    void saveProductCommand_processTest_checkReturnedMessageTextIfUserStateIsSaveProductInputProductTitle() {

        Mockito.when(userStateCache.getFromCache(data.getUserId()))
                .thenReturn(UserState.SAVE_PRODUCT_INPUT_PRODUCT_TITLE);

        PartialBotApiMethod<Message> actual = command.process(data);

        assertAll(() -> {
            assertTrue(actual instanceof SendMessage);
            assertEquals(INPUT_PRODUCT_TITLE_TEXT, ((SendMessage) actual).getText());
        });
    }

    @Test
    void saveProductCommand_processTest_checkReturnedMessageTextIfUserStateIsSaveProduct() {

        Mockito.when(userStateCache.getFromCache(data.getUserId())).thenReturn(UserState.SAVE_PRODUCT);

        ProductDto product = new ProductDto("p1");
        data.setDataForDto("product1");

        Mockito.when(productStateCache.getFromCache(data.getUserId())).thenReturn(product);
        Mockito.when(rest.saveNewProduct(product)).thenReturn(product);

        PartialBotApiMethod<Message> actual = command.process(data);

        String expectedText = String.format(PRODUCT_SAVE_MESSAGE_TEXT, product.getTitle());

        assertAll(() -> {
            assertTrue(actual instanceof SendMessage);
            assertEquals(expectedText, ((SendMessage) actual).getText());
        });
    }

    @ParameterizedTest
    @MethodSource("getUserStates")
    void saveProductCommand_processTest_checkReturnedMessageTextIfUserStateIsOther(UserState state) {

        Mockito.when(userStateCache.getFromCache(data.getUserId())).thenReturn(state);

        PartialBotApiMethod<Message> actual = command.process(data);

        assertAll(() -> {
            assertTrue(actual instanceof SendMessage);
            assertEquals(PRODUCT_SAVE_FAIL_TEXT, ((SendMessage) actual).getText());
        });
    }

    private static Stream<Arguments> getUserStates() {

        return Stream.of(
                Arguments.of(UserState.CREATE_STOCK),
                Arguments.of(UserState.MOVE_PRODUCT_INPUT_INVOICE_NUMBER),
                Arguments.of(UserState.MOVE_PRODUCT_INPUT_STOCK_FROM),
                Arguments.of(UserState.MOVE_PRODUCT_INPUT_STOCK_TO),
                Arguments.of(UserState.MOVE_PRODUCT_INPUT_PRODUCT_ARTICLE),
                Arguments.of(UserState.MOVE_PRODUCT_INPUT_PRODUCT_QUANTITY),
                Arguments.of(UserState.MOVE_PRODUCT)
        );
    }
}