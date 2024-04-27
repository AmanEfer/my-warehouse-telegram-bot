package com.amanefer.telegram.commands.stock;

import com.amanefer.telegram.cache.UserStateCache;
import com.amanefer.telegram.dto.StockDto;
import com.amanefer.telegram.services.RestToCrud;
import com.amanefer.telegram.util.Button;
import com.amanefer.telegram.util.UpdateTransferData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class GettingAllStocksCommandTest {

    @Mock
    private RestToCrud restToCrud;

    @Mock
    private UserStateCache userStateCache;

    @InjectMocks
    private GettingAllStocksCommand command;


    @Test
    void gettingAllStocksCommand_supportTest() {

        assertTrue(command.support(Button.GET_ALL_STOCKS_BUTTON.toString()));
    }

    @Test
    void gettingAllStocksCommand_processTest_checkReturnedStocksList() {

        List<StockDto> stocks = List.of(
                new StockDto("stock1"),
                new StockDto("stock2"),
                new StockDto("stock3")
        );

        Mockito.when(restToCrud.getAllStocks()).thenReturn(stocks);

        UpdateTransferData data = UpdateTransferData.builder()
                .chatId(111111)
                .userId(222222)
                .build();

        PartialBotApiMethod<Message> actual = command.process(data);

        String expectedText = """
                stock1,
                stock2,
                stock3""";

        assertAll(() -> {
            assertTrue(actual instanceof SendMessage);
            assertEquals(expectedText, ((SendMessage) actual).getText());
        });
    }
}