package com.amanefer.telegram.commands.user;

import com.amanefer.telegram.cache.UserStateCache;
import com.amanefer.telegram.dto.RoleDto;
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

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class UserDataCommandTest {

    @Mock
    private RestToCrud restToCrud;

    @Mock
    private UserStateCache userStateCache;

    @InjectMocks
    private UserDataCommand command;


    @Test
    void userDataCommand_supportTest() {

        assertTrue(command.support(Button.GET_MY_DATA_BUTTON.toString()));
    }

    @Test
    void userDataCommand_processTest_checkReturnedUserData() {

        RoleDto role = RoleDto.builder()
                .id(1)
                .name("ROLE_ADMIN")
                .build();

        UserDto user = UserDto.builder()
                .id(1)
                .username("user1")
                .role(Set.of(role))
                .build();

        UpdateTransferData data = UpdateTransferData.builder()
                .chatId(111111)
                .userId(222222)
                .build();

        Mockito.when(restToCrud.getUser(data.getUserId())).thenReturn(user);

        assertEquals(user.toString(), command.process(data).getText());
    }
}