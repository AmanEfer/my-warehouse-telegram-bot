package com.amanefer.telegram.commands.user;

import com.amanefer.telegram.cache.UserStateCache;
import com.amanefer.telegram.dto.UserDto;
import com.amanefer.telegram.services.RestToCrud;
import com.amanefer.telegram.util.Button;
import com.amanefer.telegram.util.UpdateTransferData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class AllUsersCommandTest {

    @Mock
    RestToCrud restToCrud;

    @Mock
    UserStateCache userStateCache;

    @InjectMocks
    AllUsersCommand command;


    @Test
    void allUsersCommand_supportTest() {

        assertTrue(command.support(Button.GET_ALL_USERS_BUTTON.toString()));
    }

    @Test
    void allUsersCommand_processTest_checkReturnedUsersList() {

        List<UserDto> users = List.of(
                UserDto.builder()
                        .username("user1")
                        .build(),

                UserDto.builder()
                        .username("user2")
                        .build(),

                UserDto.builder()
                        .username("user3")
                        .build()
        );

        Mockito.when(restToCrud.getUsers()).thenReturn(users);

        UpdateTransferData data = UpdateTransferData.builder()
                .chatId(111111)
                .userId(222222)
                .build();

        String expectedText = """
                user1,
                user2,
                user3""";

        SendMessage actual = command.process(data);

        assertEquals(expectedText, actual.getText());
    }
}