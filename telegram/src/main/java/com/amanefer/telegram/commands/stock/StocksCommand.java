package com.amanefer.telegram.commands.stock;

import com.amanefer.telegram.util.Button;
import com.amanefer.telegram.cache.UserStateCache;
import com.amanefer.telegram.commands.Command;
import com.amanefer.telegram.util.BotState;
import com.amanefer.telegram.util.UpdateTransferData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StocksCommand implements Command {

    public static final String MESSAGE_TEXT = """
            Choose your further action:
                        
            - you can create new warehouse
            """;

    private final UserStateCache userStateCache;


    @Override
    public boolean support(String command) {

        return command.equals(Button.STOCKS_BUTTON.getMenuName())
                || command.equals(Button.STOCKS_BUTTON.getKeyboardName());
    }


    @Override
    public PartialBotApiMethod<Message> process(UpdateTransferData updateTransferData) {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton createStockButton = InlineKeyboardButton.builder()
                .text(Button.CREATE_NEW_STOCK.getKeyboardName())
                .callbackData(Button.CREATE_NEW_STOCK.toString())
                .build();

        InlineKeyboardButton getAllStocksButton = InlineKeyboardButton.builder()
                .text(Button.GET_ALL_STOCKS_BUTTON.getKeyboardName())
                .callbackData(Button.GET_ALL_STOCKS_BUTTON.toString())
                .build();

        row.add(createStockButton);
        row.add(getAllStocksButton);

        keyboard.add(row);
        inlineKeyboardMarkup.setKeyboard(keyboard);

        userStateCache.putInCache(updateTransferData.getUserId(), BotState.PRIMARY);

        return SendMessage.builder()
                .chatId(updateTransferData.getChatId())
                .text(MESSAGE_TEXT)
                .replyMarkup(inlineKeyboardMarkup)
                .build();
    }
}
