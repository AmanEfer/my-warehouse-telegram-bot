package com.amanefer.telegram.commands;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.ArrayList;
import java.util.List;

@Getter
@Component
public class TelegramCommands {

    public static final String START_COMMAND = "/start";
    public static final String USERS_COMMAND = "/users";
    public static final String STOCK_COMMAND = "/stocks";
    public static final String PRODUCTS_COMMAND = "/products";
    public static final String EXPORT_COMMAND = "/exportfile";
    public static final String HELP_COMMAND = "/help";

    private final List<BotCommand> listOfCommands;


    {
        listOfCommands = new ArrayList<>();

        listOfCommands.add(new BotCommand(START_COMMAND, "start of application"));
        listOfCommands.add(new BotCommand(USERS_COMMAND, "work with user data"));
        listOfCommands.add(new BotCommand(STOCK_COMMAND, "work with stocks"));
        listOfCommands.add(new BotCommand(PRODUCTS_COMMAND, "work with products"));
        listOfCommands.add(new BotCommand(EXPORT_COMMAND, "export some file"));
        listOfCommands.add(new BotCommand(HELP_COMMAND, "all commands description"));
    }
}
