package com.amanefer.telegram.commands.stock;

import com.amanefer.telegram.cache.UserStateCache;
import com.amanefer.telegram.util.Button;
import com.amanefer.telegram.util.UpdateTransferData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class StocksCommandTest {

    public static final String EXPECTED_TEXT = """
            Choose your further action:
                        
            - you can create new warehouse
            """;

    @Mock
    private UserStateCache userStateCache;

    @InjectMocks
    private StocksCommand command;

    @Test
    void stocksCommand_supportTest() {

        assertAll(() -> {
            assertTrue(command.support(Button.STOCKS_BUTTON.getMenuName()));
            assertTrue(command.support(Button.STOCKS_BUTTON.getKeyboardName()));
        });
    }

    @Test
    void stocksCommand_processTest_checkReturnedReplyMarkupAndMessageText() {

        InlineKeyboardButton createStockButton = InlineKeyboardButton.builder()
                .text(Button.CREATE_NEW_STOCK.getKeyboardName())
                .callbackData(Button.CREATE_NEW_STOCK.toString())
                .build();

        InlineKeyboardButton getAllStocksButton = InlineKeyboardButton.builder()
                .text(Button.GET_ALL_STOCKS_BUTTON.getKeyboardName())
                .callbackData(Button.GET_ALL_STOCKS_BUTTON.toString())
                .build();

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(createStockButton);
        row.add(getAllStocksButton);

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(row);

        InlineKeyboardMarkup expectedKeyboardMarkup = new InlineKeyboardMarkup();
        expectedKeyboardMarkup.setKeyboard(keyboard);

        UpdateTransferData data = UpdateTransferData.builder()
                .chatId(111111)
                .userId(222222)
                .build();

        PartialBotApiMethod<Message> actual = command.process(data);

        assertAll(() -> {
            assertTrue(actual instanceof SendMessage);
            assertEquals(EXPECTED_TEXT, ((SendMessage) actual).getText());
            assertEquals(expectedKeyboardMarkup, ((SendMessage) actual).getReplyMarkup());
        });
    }
}