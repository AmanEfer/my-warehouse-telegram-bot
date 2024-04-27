package com.amanefer.telegram.commands.report;

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
class ReportCommandTest {

    @Mock
    private UserStateCache userStateCache;

    @InjectMocks
    private ReportCommand command;

    public static final String EXPECTED_TEXT = "You can download some file";


    @Test
    void reportCommand_supportTest() {

        assertAll(() -> {
            assertTrue(command.support(Button.REPORTS_BUTTON.getMenuName()));
            assertTrue(command.support(Button.REPORTS_BUTTON.getKeyboardName()));
        });
    }

    @Test
    void reportCommand_processTest_checkReturnedReplyMarkupAndMessageText() {

        InlineKeyboardButton exportFileButton = InlineKeyboardButton.builder()
                .text(Button.EXPORT_FILE_BUTTON.getKeyboardName())
                .callbackData(Button.EXPORT_FILE_BUTTON.toString())
                .build();

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(exportFileButton);

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(row);

        InlineKeyboardMarkup expectedMarkup = new InlineKeyboardMarkup();
        expectedMarkup.setKeyboard(keyboard);

        UpdateTransferData data = UpdateTransferData.builder()
                .chatId(111111)
                .userId(222222)
                .build();

        PartialBotApiMethod<Message> actual = command.process(data);

        assertAll(() -> {
            assertTrue(actual instanceof SendMessage);
            assertEquals(EXPECTED_TEXT, ((SendMessage) actual).getText());
            assertEquals(expectedMarkup, ((SendMessage) actual).getReplyMarkup());
        });
    }
}