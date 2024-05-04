package com.amanefer.telegram.commands;

import com.amanefer.telegram.cache.UserStateCache;
import com.amanefer.telegram.services.RestToCrud;
import com.amanefer.telegram.util.Button;
import com.amanefer.telegram.util.UpdateTransferData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class StartCommandTest {

    private static final String MESSAGE_TEXT = """
            Hi, %s, nice to meet you!
                        
            Choose your next action:
            """;

    @Mock
    private RestToCrud restToCrud;

    @Mock
    private UserStateCache userStateCache;

    @InjectMocks
    private StartCommand command;


    @Test
    void startCommand_supportTest() {

        assertAll(() -> {
            assertTrue(command.support(Button.START_BUTTON.getMenuName()));
            assertTrue(command.support(Button.START_BUTTON.getKeyboardName()));
        });
    }

    @Test
    void startCommand_processTest_checkReturnedReplyMarkupAndMessageText() {

        KeyboardButton stocksButton = new KeyboardButton(Button.STOCKS_BUTTON.getKeyboardName());
        KeyboardButton productsButton = new KeyboardButton(Button.PRODUCTS_BUTTON.getKeyboardName());
        KeyboardButton usersButton = new KeyboardButton(Button.USERS_BUTTON.getKeyboardName());
        KeyboardButton reportsButton = new KeyboardButton(Button.REPORTS_BUTTON.getKeyboardName());
        KeyboardButton helpButton = new KeyboardButton(Button.HELP_BUTTON.getKeyboardName());

        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();

        row1.add(stocksButton);
        row1.add(productsButton);
        row2.add(usersButton);
        row2.add(reportsButton);
        row3.add(helpButton);

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);

        ReplyKeyboardMarkup expectedKeyboardMarkup = new ReplyKeyboardMarkup();
        expectedKeyboardMarkup.setSelective(true);
        expectedKeyboardMarkup.setResizeKeyboard(true);
        expectedKeyboardMarkup.setOneTimeKeyboard(true);
        expectedKeyboardMarkup.setKeyboard(keyboard);

        UpdateTransferData data = UpdateTransferData.builder()
                .chatId(111111)
                .userId(222222)
                .userName("user1")
                .build();

        SendMessage actual = command.process(data);

        String expectedText = String.format(MESSAGE_TEXT, data.getUserName());

        assertAll(() -> {
            assertEquals(expectedText, actual.getText());
            assertEquals(expectedKeyboardMarkup, actual.getReplyMarkup());
        });
    }
}