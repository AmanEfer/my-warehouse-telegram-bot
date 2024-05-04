package com.amanefer.telegram.commands.stock;

import com.amanefer.telegram.cache.UserStateCache;
import com.amanefer.telegram.dto.StockDto;
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
class CreateStockCommandTest {

    public static final String INPUT_WAREHOUSE_NAME_TEXT = "Please, input your new warehouse's name";
    public static final String WAREHOUSE_CREATE_MESSAGE_TEXT = "Warehouse '%s' was created successfully!";
    public static final String STOCK_CREATE_FAIL_TEXT = "Warehouse wasn't created, try again";


    @Mock
    private RestToCrud rest;

    @Mock
    private UserStateCache userStateCache;

    @InjectMocks
    CreateStockCommand command;

    private UpdateTransferData data;


    @BeforeEach
    void init() {

        data = UpdateTransferData.builder()
                .chatId(111111)
                .userId(222222)
                .build();
    }

    @Test
    void createStockCommand_supportTest() {

        assertTrue(command.support(Button.CREATE_NEW_STOCK.toString()));
    }

    @Test
    void createStockCommand_processTest_checkReturnedMessageTextIfUserStateIsPrimary() {

        Mockito.when(userStateCache.getFromCache(data.getUserId())).thenReturn(UserState.PRIMARY);

        PartialBotApiMethod<Message> actual = command.process(data);

        assertAll(() -> {
            assertTrue(actual instanceof SendMessage);
            assertEquals(INPUT_WAREHOUSE_NAME_TEXT, ((SendMessage) actual).getText());
        });
    }

    @Test
    void createStockCommand_processTest_checkReturnedMessageTextIfUserStateIsCreateStock() {

        Mockito.when(userStateCache.getFromCache(data.getUserId())).thenReturn(UserState.CREATE_STOCK);

        StockDto stock = new StockDto("stock1");

        data.setDataForDto(stock.getStockName());

        Mockito.when(rest.saveNewStock(stock)).thenReturn(stock);

        PartialBotApiMethod<Message> actual = command.process(data);

        String expectedText = String.format(WAREHOUSE_CREATE_MESSAGE_TEXT, stock.getStockName());

        assertAll(() -> {
            assertTrue(actual instanceof SendMessage);
            assertEquals(expectedText, ((SendMessage) actual).getText());
        });
    }

    @ParameterizedTest
    @MethodSource("getUserStates")
    void createStockCommand_processTest_checkReturnedMessageTextIfUserStateIsOther(UserState state) {

        Mockito.when(userStateCache.getFromCache(data.getUserId())).thenReturn(state);

        PartialBotApiMethod<Message> actual = command.process(data);

        assertAll(() -> {
            assertTrue(actual instanceof SendMessage);
            assertEquals(STOCK_CREATE_FAIL_TEXT, ((SendMessage) actual).getText());
        });
    }

    private static Stream<Arguments> getUserStates() {

        return Stream.of(
                Arguments.of(UserState.SAVE_PRODUCT_INPUT_PRODUCT_TITLE),
                Arguments.of(UserState.SAVE_PRODUCT),
                Arguments.of(UserState.MOVE_PRODUCT_INPUT_INVOICE_NUMBER),
                Arguments.of(UserState.MOVE_PRODUCT_INPUT_STOCK_FROM),
                Arguments.of(UserState.MOVE_PRODUCT_INPUT_STOCK_TO),
                Arguments.of(UserState.MOVE_PRODUCT_INPUT_PRODUCT_ARTICLE),
                Arguments.of(UserState.MOVE_PRODUCT_INPUT_PRODUCT_QUANTITY),
                Arguments.of(UserState.MOVE_PRODUCT)
        );
    }
}