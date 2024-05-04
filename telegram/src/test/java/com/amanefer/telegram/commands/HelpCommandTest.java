package com.amanefer.telegram.commands;

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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class HelpCommandTest {

    private static final String EXPECTED_TEXT = """
            You can use one of this commands:
                        
                        
            /start - this button launches the bot and registers you in this bot
                        
            /users - this button give you opportunity to get your data
                        
            /stocks - you can use this button to work with warehouses: create, modify or delete warehouses
                        
            /products - this button allows you to work with goods: add, delete, sell or move goods between warehouses
                        
            /reports - you can receive various reports on the goods available in warehouses as a file
                        
            /help - instruction for the buttons
            """;

    @Mock
    private UserStateCache userStateCache;

    @InjectMocks
    private HelpCommand command;


    @Test
    void helpCommand_supportTest() {

        assertAll(() -> {
            assertTrue(command.support(Button.HELP_BUTTON.getMenuName()));
            assertTrue(command.support(Button.HELP_BUTTON.getKeyboardName()));
        });
    }

    @Test
    void helpCommand_processTest_checkReturnedMessageText() {

        UpdateTransferData data = UpdateTransferData.builder()
                .chatId(111111)
                .build();

        PartialBotApiMethod<Message> actual = command.process(data);

        assertAll(() -> {
            assertTrue(actual instanceof SendMessage);
            assertEquals(EXPECTED_TEXT, ((SendMessage) actual).getText());
        });
    }
}