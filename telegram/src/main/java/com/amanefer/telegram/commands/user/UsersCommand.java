package com.amanefer.telegram.commands.user;

import com.amanefer.telegram.cache.UserStateCache;
import com.amanefer.telegram.commands.Command;
import com.amanefer.telegram.dto.RoleDto;
import com.amanefer.telegram.services.RestToCrud;
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
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UsersCommand implements Command {

    private static final String ADMIN_TEXT = "Which data would you want to get?";
    private static final String USER_TEXT = "You can look at your data";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    private final RestToCrud restToCrud;
    private final UserStateCache userStateCache;
    private static final InlineKeyboardMarkup USERS_KEYBOARD_ADMIN;
    private static final InlineKeyboardMarkup USERS_KEYBOARD_USER;

    static {
        USERS_KEYBOARD_ADMIN = buildKeyboard(true);
        USERS_KEYBOARD_USER = buildKeyboard(false);
    }


    public UsersCommand(RestToCrud restToCrud, UserStateCache userStateCache) {
        this.restToCrud = restToCrud;
        this.userStateCache = userStateCache;
    }

    @Override
    public boolean support(String command) {

        return command.equals(Button.USERS_BUTTON.getMenuName())
                || command.equals(Button.USERS_BUTTON.getKeyboardName());
    }

    @Override
    public PartialBotApiMethod<Message> process(UpdateTransferData updateTransferData) {

        Set<String> rolesNames = restToCrud.getUser(updateTransferData.getUserId())
                .getRole().stream()
                .map(RoleDto::getName)
                .collect(Collectors.toSet());

        boolean isAdmin = rolesNames.contains(ROLE_ADMIN);

        userStateCache.putInCache(updateTransferData.getUserId(), UserState.PRIMARY);

        return SendMessage.builder()
                .chatId(updateTransferData.getChatId())
                .text(isAdmin ? ADMIN_TEXT : USER_TEXT)
                .replyMarkup(isAdmin ? USERS_KEYBOARD_ADMIN : USERS_KEYBOARD_USER)
                .build();
    }

    private static InlineKeyboardMarkup buildKeyboard(boolean isAdmin) {

        List<InlineKeyboardButton> row = new ArrayList<>();

        if (isAdmin) {
            InlineKeyboardButton allUsersButton = InlineKeyboardButton.builder()
                    .text(Button.GET_ALL_USERS_BUTTON.getKeyboardName())
                    .callbackData(Button.GET_ALL_USERS_BUTTON.toString())
                    .build();

            row.add(allUsersButton);
        }

        InlineKeyboardButton userDataButton = InlineKeyboardButton.builder()
                .text(Button.GET_MY_DATA_BUTTON.getKeyboardName())
                .callbackData(Button.GET_MY_DATA_BUTTON.toString())
                .build();

        row.add(userDataButton);

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(row);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(keyboard);

        return inlineKeyboardMarkup;
    }

}
