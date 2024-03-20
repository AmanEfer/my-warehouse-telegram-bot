package com.amanefer.telegram.commands;

import com.amanefer.telegram.dto.RoleDto;
import com.amanefer.telegram.dto.UserDto;
import com.amanefer.telegram.services.RestToCrud;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserDataCommandTest {

    private static final String GET_MY_DATA = "get my data";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final Long CHAT_ID = 123456L;

    private Command command;

    @Mock
    private RestToCrud rest;
    @Mock
    private Message msg;


    @BeforeEach
    public void init() {

        command = new UserDataCommand(rest);
    }

    @Test
    public void userDataCommand_supportTest() {

        assertTrue(command.support(GET_MY_DATA));
    }

    @Test
    public void userDataCommand_processTest_checkReturnedMessage() {

        User user = new User();
        user.setId(1L);

        UserDto userDto = new UserDto(1, "user1", Set.of(new RoleDto(1, ROLE_ADMIN)));

        Mockito.when(msg.getFrom()).thenReturn(user);
        Mockito.when(rest.getUser(Mockito.anyLong())).thenReturn(userDto);
        Mockito.when(msg.getChatId()).thenReturn(CHAT_ID);

        String expected = """
                id: 1
                username: user1
                role: admin""";

        SendMessage actual = (SendMessage) command.process(msg);

        assertEquals(expected, actual.getText());
    }

}