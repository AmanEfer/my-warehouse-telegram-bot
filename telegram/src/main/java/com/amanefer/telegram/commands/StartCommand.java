package com.amanefer.telegram.commands;

import com.amanefer.telegram.util.Button;
import com.amanefer.telegram.cache.UserStateCache;
import com.amanefer.telegram.dto.UserDto;
import com.amanefer.telegram.services.RestToCrud;
import com.amanefer.telegram.util.UserState;
import com.amanefer.telegram.util.UpdateTransferData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StartCommand implements Command {

    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String MESSAGE_TEXT = """
            Hi, %s, nice to meet you!
                        
            Choose your next action:
            """;

    @Value("${bot.admin}")
    private long adminId;

    private final RestToCrud rest;
    private final UserStateCache userStateCache;


    @Override
    public boolean support(String command) {

        return command.equals(Button.START_BUTTON.getMenuName())
                || command.equals(Button.START_BUTTON.getKeyboardName());
    }

    @Override
    public SendMessage process(UpdateTransferData updateTransferData) {

        long chatId = updateTransferData.getChatId();
        long userId = updateTransferData.getUserId();
        String userName = updateTransferData.getUserName();
        String answer = String.format(MESSAGE_TEXT, userName);

        UserDto user = UserDto.builder()
                .id(userId)
                .username(userName)
                .build();

        rest.registerNewUser(user, userId == adminId ? ROLE_ADMIN : ROLE_USER);

        userStateCache.putInCache(userId, UserState.PRIMARY);

        return createStartMessageWithKeyboard(chatId, answer);
    }

    private SendMessage createStartMessageWithKeyboard(long chatId, String textToSend) {

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton(Button.STOCKS_BUTTON.getKeyboardName()));
        row.add(new KeyboardButton(Button.PRODUCTS_BUTTON.getKeyboardName()));
        keyboard.add(row);

        row = new KeyboardRow();
        row.add(new KeyboardButton(Button.USERS_BUTTON.getKeyboardName()));
        row.add(new KeyboardButton(Button.REPORTS_BUTTON.getKeyboardName()));
        keyboard.add(row);

        row = new KeyboardRow();
        row.add(new KeyboardButton(Button.HELP_BUTTON.getKeyboardName()));
        keyboard.add(row);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return SendMessage.builder()
                .chatId(chatId)
                .text(textToSend)
                .replyMarkup(replyKeyboardMarkup)
                .build();
    }

}
