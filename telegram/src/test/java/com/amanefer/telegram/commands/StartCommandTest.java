package com.amanefer.telegram.commands;

import com.amanefer.telegram.services.RestToCrud;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StartCommandTest {

    private static final String START_BUTTON = "/start";
    private static final Long CHAT_ID = 123456L;

    private Command command;

    @Mock
    private Message msg;

    @Mock
    private RestToCrud rest;

    @Mock
    User user;


    @BeforeEach
    public void init() {

        command = new StartCommand(rest);
    }

    @Test
    public void startCommand_supportTest() {

        assertTrue(command.support(START_BUTTON));
    }

    @Test
    public void startCommand_processTest_checkReturnedMessage() {

        Mockito.when(msg.getFrom()).thenReturn(user);

        Mockito.when(msg.getFrom().getId()).thenReturn(654321L);

        Mockito.when(msg.getChatId()).thenReturn(CHAT_ID);

        Chat chat = new Chat();
        chat.setUserName("user1");

        Mockito.when(msg.getChat()).thenReturn(chat);

        String expected = """
            Hi, user1, nice to meet you!
                        
            Choose your next action:
            """;

        SendMessage actual = (SendMessage) command.process(msg);

        assertEquals(expected, actual.getText());
    }

}