package com.amanefer.telegram.commands;

import com.amanefer.telegram.cache.StatesCache;
import com.amanefer.telegram.dto.UserDto;
import com.amanefer.telegram.services.RestToCrud;
import com.amanefer.telegram.state.BotState;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
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
    public static final String START_COMMAND = "/start";
    public static final String USERS_COMMAND = "users";
    public static final String STOCKS_COMMAND = "stocks";
    public static final String PRODUCTS_COMMAND = "products";
    public static final String HELP_COMMAND = "help";
    private static final String MESSAGE_TEXT = """
            Hi, %s, nice to meet you!
                        
            Choose your next action:
            """;

    @Value("${bot.admin}")
    private long adminId;

    private final RestToCrud rest;
    private final StatesCache statesCache;


    @Override
    public boolean support(String command) {

        return command.equalsIgnoreCase(START_COMMAND);
    }

    @Override
    public SendMessage process(Message msg) {

        long userId = msg.getFrom().getId();
        long chatId = msg.getChatId();
        String userName = msg.getChat().getUserName();

        String answer = String.format(MESSAGE_TEXT, userName);

        registerUser(userId, userName);

        statesCache.setUserStateCache(userId, BotState.PRIMARY);

        return createStartMessageWithKeyboard(chatId, answer);
    }

    private void registerUser(long userId, String userName) {

        UserDto user = UserDto.builder()
                .id(userId)
                .username(userName)
                .build();

        rest.registerNewUser(user, userId == adminId ? ROLE_ADMIN : ROLE_USER);
    }

    private SendMessage createStartMessageWithKeyboard(long chatId, String textToSend) {

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton(USERS_COMMAND));
        keyboard.add(row);

        row = new KeyboardRow();
        row.add(new KeyboardButton(STOCKS_COMMAND));
        keyboard.add(row);

        row = new KeyboardRow();
        row.add(new KeyboardButton(PRODUCTS_COMMAND));
        keyboard.add(row);

        row = new KeyboardRow();
        row.add(new KeyboardButton("export file"));
        keyboard.add(row);

        row = new KeyboardRow();
        row.add(new KeyboardButton(HELP_COMMAND));
        keyboard.add(row);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return SendMessage.builder()
                .chatId(chatId)
                .text(textToSend)
                .replyMarkup(replyKeyboardMarkup)
                .build();
    }

}
