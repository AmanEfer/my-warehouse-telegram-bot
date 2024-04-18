package com.amanefer.telegram.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class UsersCommand implements Command {
    @Override
    public boolean support(String command) {
        return command.matches("/?users");
    }

    @Override
    public PartialBotApiMethod<Message> process(Message msg) {
        return null;
    }
}
