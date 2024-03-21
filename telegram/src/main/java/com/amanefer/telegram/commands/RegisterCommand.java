package com.amanefer.telegram.commands;

import com.amanefer.telegram.dto.UserDto;
import com.amanefer.telegram.services.RestToCrud;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class RegisterCommand implements Command {

    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String REGISTER_COMMAND = "/register";
    public static final String REGISTER_NEW_USER_COMMAND = "register new user";
    public static final String MESSAGE_TEXT = "User with username '%s' was registered";


    private final RestToCrud rest;


    @Override
    public boolean support(String command) {

        return command.equalsIgnoreCase(REGISTER_COMMAND)
                || command.equalsIgnoreCase(REGISTER_NEW_USER_COMMAND);
    }

    @Override
    public SendMessage process(Message msg) {

        long userId = msg.getFrom().getId();
        String userName = msg.getFrom().getUserName();

        UserDto user = new UserDto();
        user.setId(userId);
        user.setUsername(userName);

        user = rest.registerNewUser(user, ROLE_ADMIN);

        return new SendMessage(String.valueOf(msg.getChatId()),
                String.format(MESSAGE_TEXT, user.getUsername()));
    }

}
