package com.amanefer.telegram.commands.user;

import com.amanefer.telegram.cache.UserStateCache;
import com.amanefer.telegram.dto.RoleDto;
import com.amanefer.telegram.dto.UserDto;
import com.amanefer.telegram.services.RestToCrud;
import com.amanefer.telegram.util.Button;
import com.amanefer.telegram.util.UpdateTransferData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class UsersCommandTest {

    @Mock
    private RestToCrud restToCrud;

    @Mock
    private UserStateCache userStateCache;

    @InjectMocks
    private UsersCommand command;

    private static final String ADMIN_TEXT = "Which data would you want to get?";
    private static final String USER_TEXT = "You can look at your data";

    private UpdateTransferData data;
    private UserDto user;
    private InlineKeyboardButton userDataButton;
    private InlineKeyboardMarkup expectedMarkup;
    private List<List<InlineKeyboardButton>> keyboard;
    private List<InlineKeyboardButton> row;


    @BeforeEach
    void init() {

        data = UpdateTransferData.builder()
                .chatId(111111)
                .userId(222222)
                .build();

        user = UserDto.builder()
                .id(1)
                .username("user1")
                .build();

        userDataButton = InlineKeyboardButton.builder()
                .text(Button.GET_MY_DATA_BUTTON.getKeyboardName())
                .callbackData(Button.GET_MY_DATA_BUTTON.toString())
                .build();

        keyboard = new ArrayList<>();
        row = new ArrayList<>();
        expectedMarkup = new InlineKeyboardMarkup();
    }

    @Test
    void usersCommand_supportTest() {

        assertAll(() -> {
            assertTrue(command.support(Button.USERS_BUTTON.getMenuName()));
            assertTrue(command.support(Button.USERS_BUTTON.getKeyboardName()));
        });
    }

    @Test
    void usersCommand_processTest_checkReturnedReplyMarkupAndMessageTextIfRoleIsAdmin() {

        user.setRole(Set.of(new RoleDto(1, "ROLE_ADMIN")));

        Mockito.when(restToCrud.getUser(data.getUserId())).thenReturn(user);

        InlineKeyboardButton allUsersButton = InlineKeyboardButton.builder()
                .text(Button.GET_ALL_USERS_BUTTON.getKeyboardName())
                .callbackData(Button.GET_ALL_USERS_BUTTON.toString())
                .build();

        row.add(allUsersButton);
        row.add(userDataButton);
        keyboard.add(row);
        expectedMarkup.setKeyboard(keyboard);

        PartialBotApiMethod<Message> actual = command.process(data);

        assertAll(() -> {
            assertTrue(actual instanceof SendMessage);
            assertEquals(ADMIN_TEXT, ((SendMessage) actual).getText());
            assertEquals(expectedMarkup, ((SendMessage) actual).getReplyMarkup());
        });
    }

    @Test
    void usersCommand_processTest_checkReturnedReplyMarkupAndMessageTextIfRoleIsUser() {

        user.setRole(Set.of(new RoleDto(1, "ROLE_USER")));

        Mockito.when(restToCrud.getUser(data.getUserId())).thenReturn(user);

        row.add(userDataButton);
        keyboard.add(row);
        expectedMarkup.setKeyboard(keyboard);

        PartialBotApiMethod<Message> actual = command.process(data);

        assertAll(() -> {
            assertTrue(actual instanceof SendMessage);
            assertEquals(USER_TEXT, ((SendMessage) actual).getText());
            assertEquals(expectedMarkup, ((SendMessage) actual).getReplyMarkup());
        });
    }
}
