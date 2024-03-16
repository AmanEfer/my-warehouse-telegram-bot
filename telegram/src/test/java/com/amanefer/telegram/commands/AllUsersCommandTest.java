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
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class AllUsersCommandTest {

    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_USER = "ROLE_USER";
    private static final String GET_ALL_USERS_BUTTON = "get all users";
    private static final long CHAT_ID = 12345;

    private Command command;

    @Mock
    private RestToCrud rest;
    @Mock
    private Message msg;


    @BeforeEach
    public void init() {
        command = new AllUsersCommand(rest);
    }

    @Test
    public void allUsersCommand_supportTest() {
        assertTrue(command.support(GET_ALL_USERS_BUTTON));
    }

    @Test
    public void allUsersCommand_processTest_checkReturnedMessage() {
        List<UserDto> userDtoList = List.of(
                new UserDto(1, "user1", Set.of(new RoleDto(1, ROLE_ADMIN))),
                new UserDto(2, "user2", Set.of(new RoleDto(2, ROLE_USER))),
                new UserDto(3, "user3", Set.of(new RoleDto(2, ROLE_USER)))
        );

        Mockito.when(rest.getUsers()).thenReturn(userDtoList);
        Mockito.when(msg.getChatId()).thenReturn(CHAT_ID);

        String expected = """
                user1
                user2
                user3""";

        assertEquals(expected, command.process(msg).getText());
    }
}
