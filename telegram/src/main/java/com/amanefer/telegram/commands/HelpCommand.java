package com.amanefer.telegram.commands;

import com.amanefer.telegram.util.Button;
import com.amanefer.telegram.cache.UserStateCache;
import com.amanefer.telegram.util.UserState;
import com.amanefer.telegram.util.UpdateTransferData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class HelpCommand implements Command {

    private static final String MESSAGE_TEXT = """
            You can use one of this commands:
                        
                        
            /start - this button launches the bot and registers you in this bot
                        
            /users - this button give you opportunity to get your data
                        
            /stocks - you can use this button to work with warehouses: create, modify or delete warehouses
                        
            /products - this button allows you to work with goods: add, delete, sell or move goods between warehouses
                        
            /reports - you can receive various reports on the goods available in warehouses as a file
                        
            /help - instruction for the buttons
            """;

    private final UserStateCache userStateCache;


    @Override
    public boolean support(String command) {

        return command.equals(Button.HELP_BUTTON.getMenuName())
                || command.equals(Button.HELP_BUTTON.getKeyboardName());
    }

    @Override
    public PartialBotApiMethod<Message> process(UpdateTransferData updateTransferData) {

        userStateCache.putInCache(updateTransferData.getUserId(), UserState.PRIMARY);

        return SendMessage.builder()
                .chatId(updateTransferData.getChatId())
                .text(MESSAGE_TEXT)
                .build();
    }

}
