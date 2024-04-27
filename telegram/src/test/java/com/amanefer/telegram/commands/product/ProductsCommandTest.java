package com.amanefer.telegram.commands.product;

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
class ProductsCommandTest {

    private static final String EXPECTED_TEXT = """
            Choose your further action:
                        
            - you can save new product in data base
            - you can see the list of the all products
            """;

    @Mock
    private UserStateCache userStateCache;

    @InjectMocks
    private ProductsCommand command;


    @Test
    void productsCommand_supportTest() {

        assertAll(() -> {
            assertTrue(command.support(Button.PRODUCTS_BUTTON.getMenuName()));
            assertTrue(command.support(Button.PRODUCTS_BUTTON.getKeyboardName()));
        });
    }

    @Test
    void productsCommand_processTest_checkReturnedReplyMarkupAndMessageText() {

        InlineKeyboardButton saveProductButton = InlineKeyboardButton.builder()
                .text(Button.SAVE_NEW_PRODUCT.getKeyboardName())
                .callbackData(Button.SAVE_NEW_PRODUCT.toString())
                .build();

        InlineKeyboardButton getAllProductsButton = InlineKeyboardButton.builder()
                .text(Button.GET_ALL_PRODUCTS_BUTTON.getKeyboardName())
                .callbackData(Button.GET_ALL_PRODUCTS_BUTTON.toString())
                .build();

        InlineKeyboardButton moveProductsButton = InlineKeyboardButton.builder()
                .text(Button.MOVE_PRODUCTS_BUTTON.getKeyboardName())
                .callbackData(Button.MOVE_PRODUCTS_BUTTON.toString())
                .build();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(saveProductButton);
        row1.add(getAllProductsButton);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(moveProductsButton);

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(row1);
        keyboard.add(row2);

        InlineKeyboardMarkup expectedKeyboardMarkup = new InlineKeyboardMarkup();
        expectedKeyboardMarkup.setKeyboard(keyboard);

        UpdateTransferData data = UpdateTransferData.builder()
                .userId(111111)
                .chatId(222222)
                .build();

        PartialBotApiMethod<Message> actual = command.process(data);

        assertAll(() -> {
            assertTrue(actual instanceof SendMessage);
            assertEquals(EXPECTED_TEXT, ((SendMessage) actual).getText());
            assertEquals(expectedKeyboardMarkup, ((SendMessage) actual).getReplyMarkup());
        });
    }
}