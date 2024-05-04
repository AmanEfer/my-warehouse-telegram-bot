package com.amanefer.telegram.commands;

import com.amanefer.telegram.util.Button;
import com.amanefer.telegram.cache.UserStateCache;
import com.amanefer.telegram.dto.UserDto;
import com.amanefer.telegram.services.RestToCrud;
import com.amanefer.telegram.util.UserState;
import com.amanefer.telegram.util.UpdateTransferData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class StartCommand implements Command {

    private static final ReplyKeyboardMarkup START_KEYBOARD;
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String MESSAGE_TEXT = """
            Hi, %s, nice to meet you!
                        
            Choose your next action:
            """;

    private final RestToCrud rest;
    private final UserStateCache userStateCache;

    @Value("${bot.admin}")
    private long adminId;

    static {
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();

        row1.add(new KeyboardButton(Button.STOCKS_BUTTON.getKeyboardName()));
        row1.add(new KeyboardButton(Button.PRODUCTS_BUTTON.getKeyboardName()));
        row2.add(new KeyboardButton(Button.USERS_BUTTON.getKeyboardName()));
        row2.add(new KeyboardButton(Button.REPORTS_BUTTON.getKeyboardName()));
        row3.add(new KeyboardButton(Button.HELP_BUTTON.getKeyboardName()));

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);

        START_KEYBOARD = new ReplyKeyboardMarkup();
        START_KEYBOARD.setSelective(true);
        START_KEYBOARD.setResizeKeyboard(true);
        START_KEYBOARD.setOneTimeKeyboard(true);
        START_KEYBOARD.setKeyboard(keyboard);
    }

    public StartCommand(RestToCrud rest, UserStateCache userStateCache) {
        this.rest = rest;
        this.userStateCache = userStateCache;
    }

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

        return SendMessage.builder()
                .chatId(chatId)
                .text(answer)
                .replyMarkup(START_KEYBOARD)
                .build();
    }

}
