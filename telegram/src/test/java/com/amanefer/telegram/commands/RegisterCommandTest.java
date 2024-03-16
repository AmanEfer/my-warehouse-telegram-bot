package com.amanefer.telegram.commands;

import com.amanefer.telegram.dto.RoleDto;
import com.amanefer.telegram.dto.UserDto;
import com.amanefer.telegram.services.RestToCrud;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class RegisterCommandTest {

    private static final String REGISTER_BUTTON = "/register";
    private static final String REGISTER_NEW_USER_BUTTON = "register new user";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final Long CHAT_ID = 123456L;

    private Command command;

    @Mock
    private RestToCrud rest;
    @Mock
    private Message msg;


    @BeforeEach
    public void init() {
        command = new RegisterCommand(rest);
    }

    @ParameterizedTest
    @CsvSource({REGISTER_BUTTON, REGISTER_NEW_USER_BUTTON})
    public void registerCommand_supportTest(String commandName) {
        assertTrue(command.support(commandName));
    }

    @Test
    public void registerCommand_processTest_checkReturnedMessage() {
        User user = new User();
        user.setId(1L);
        user.setUserName("user1");

        Mockito.when(msg.getFrom()).thenReturn(user);

        UserDto userDto = new UserDto(user.getId(), user.getUserName(), Set.of(new RoleDto(1, ROLE_ADMIN)));

        Mockito.when(rest.registerNewUser(Mockito.any(UserDto.class), Mockito.anyString())).thenReturn(userDto);
        Mockito.when(msg.getChatId()).thenReturn(CHAT_ID);

        String expected = "User with username 'user1' was registered";

        assertEquals(expected, command.process(msg).getText());
    }
}