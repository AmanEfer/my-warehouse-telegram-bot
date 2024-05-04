package com.amanefer.telegram.commands.product;

import com.amanefer.telegram.cache.UserStateCache;
import com.amanefer.telegram.commands.Command;
import com.amanefer.telegram.util.Button;
import com.amanefer.telegram.util.UpdateTransferData;
import com.amanefer.telegram.util.UserState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductsCommand implements Command {

    private static final InlineKeyboardMarkup PRODUCTS_KEYBOARD;
    private static final String MESSAGE_TEXT = """
            Choose your further action:
                        
            - you can save new product in data base
            - you can see the list of the all products
            """;

    private final UserStateCache userStateCache;

    static {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();

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

        row1.add(saveProductButton);
        row1.add(getAllProductsButton);
        row2.add(moveProductsButton);

        keyboard.add(row1);
        keyboard.add(row2);

        PRODUCTS_KEYBOARD = new InlineKeyboardMarkup();
        PRODUCTS_KEYBOARD.setKeyboard(keyboard);
    }

    public ProductsCommand(UserStateCache userStateCache) {
        this.userStateCache = userStateCache;
    }

    @Override
    public boolean support(String command) {

        return command.equals(Button.PRODUCTS_BUTTON.getMenuName())
                || command.equals(Button.PRODUCTS_BUTTON.getKeyboardName());
    }

    @Override
    public PartialBotApiMethod<Message> process(UpdateTransferData updateTransferData) {

        userStateCache.putInCache(updateTransferData.getUserId(), UserState.PRIMARY);

        return SendMessage.builder()
                .chatId(updateTransferData.getChatId())
                .text(MESSAGE_TEXT)
                .replyMarkup(PRODUCTS_KEYBOARD)
                .build();
    }
}
