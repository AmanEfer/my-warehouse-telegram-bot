package com.amanefer.telegram.commands;

import com.amanefer.telegram.dto.UserDto;
import com.amanefer.telegram.services.RestToCrud;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AllUsersCommand implements Command {

    public static final String GET_ALL_USERS_COMMAND = "get all users";

    private final RestToCrud rest;


    @Override
    public boolean support(String command) {

        return command.equalsIgnoreCase(GET_ALL_USERS_COMMAND);
    }

    @Override
    public SendMessage process(Message msg) {

        String result = rest.getUsers().stream()
                .map(UserDto::getUsername)
                .collect(Collectors.joining("\n"));

        return new SendMessage(String.valueOf(msg.getChatId()), result);
    }

}
