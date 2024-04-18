package com.amanefer.telegram.commands;

import com.amanefer.telegram.services.RestToCrud;
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
public class UsersCommand implements Command {

    private static final String ADMIN_TEXT = "Which data would you want to get?";
    private static final String USER_TEXT = "You can look at your data";

    private final RestToCrud restToCrud;


    @Override
    public boolean support(String command) {
        return command.matches("/?users");
    }

    @Override
    public PartialBotApiMethod<Message> process(Message msg) {

        long userId = msg.getFrom().getId();
        long chatId = msg.getChat().getId();

        return createUsersMessageWithInlineKeyboard(userId, chatId);
    }

    private SendMessage createUsersMessageWithInlineKeyboard(long userId, long chatId) {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        boolean isAdmin = restToCrud.getUser(userId).getRoles().contains("ROLE_ADMIN");

        if (isAdmin) {
            InlineKeyboardButton allUsersButton = InlineKeyboardButton.builder()
                    .text("get all users")
                    .callbackData("GET_ALL_USERS_BUTTON")
                    .build();

            row.add(allUsersButton);
        }

        InlineKeyboardButton userDataButton = InlineKeyboardButton.builder()
                .text("get my data")
                .callbackData("GET_MY_DATA_BUTTON")
                .build();

        row.add(userDataButton);

        keyboard.add(row);
        inlineKeyboardMarkup.setKeyboard(keyboard);

        return SendMessage.builder()
                .chatId(chatId)
                .text(isAdmin ? ADMIN_TEXT : USER_TEXT)
                .replyMarkup(inlineKeyboardMarkup)
                .build();
    }
}
