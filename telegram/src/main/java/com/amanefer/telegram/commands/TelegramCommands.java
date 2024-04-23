package com.amanefer.telegram.commands;

import com.amanefer.telegram.util.Button;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.ArrayList;
import java.util.List;

@Getter
@Component
public class TelegramCommands {

    private final List<BotCommand> listOfCommands = new ArrayList<>();

    {
        listOfCommands.add(new BotCommand(Button.START_BUTTON.getMenuName(), "start bot"));
        listOfCommands.add(new BotCommand(Button.USERS_BUTTON.getMenuName(), "work with user data"));
        listOfCommands.add(new BotCommand(Button.STOCKS_BUTTON.getMenuName(), "work with stocks"));
        listOfCommands.add(new BotCommand(Button.PRODUCTS_BUTTON.getMenuName(), "work with products"));
        listOfCommands.add(new BotCommand(Button.REPORTS_BUTTON.getMenuName(), "export some file"));
        listOfCommands.add(new BotCommand(Button.HELP_BUTTON.getMenuName(), "all commands description"));
    }
}
