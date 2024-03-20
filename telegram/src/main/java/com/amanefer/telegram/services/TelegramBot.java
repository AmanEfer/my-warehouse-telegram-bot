package com.amanefer.telegram.services;

import com.amanefer.telegram.commands.Command;
import com.amanefer.telegram.config.BotConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Setter
@Component
public class TelegramBot extends TelegramLongPollingBot {

    public static final String START_COMMAND = "/start";
    public static final String REGISTER_COMMAND = "/register";
    public static final String EXPORT_COMMAND = "/export";
    public static final String DEFAULT_MESSAGE_TEXT = "Sorry, command wasn't recognize";

    private final BotConfig botConfig;
    private final List<Command> commands;

    public TelegramBot(BotConfig botConfig, List<Command> commands) {

        this.botConfig = botConfig;
        this.commands = commands;

        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand(START_COMMAND, "start of application"));
        listOfCommands.add(new BotCommand(REGISTER_COMMAND, "to register a new user"));
        listOfCommands.add(new BotCommand(EXPORT_COMMAND, "export some file"));

        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));

        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list: " + e.getMessage());
        }
    }


    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            PartialBotApiMethod<Message> message = commands.stream()
                    .filter(c -> c.support(messageText))
                    .findFirst()
                    .map(c -> c.process(update.getMessage()))
                    .orElseGet(() -> getDefaultSendMessage(chatId));

            castAndExecuteSending(message);
        }
    }

    private void castAndExecuteSending(PartialBotApiMethod<Message> message) {

        try {
            if (message instanceof SendMessage sendMessage) {
                execute(sendMessage);

            } else if (message instanceof SendDocument sendDocument) {
                execute(sendDocument);
            }

        } catch (TelegramApiException e) {
            log.error("Error occurred " + e.getMessage());
        }
    }

    private SendMessage getDefaultSendMessage(long chatId) {

        return SendMessage.builder()
                .chatId(chatId)
                .text(DEFAULT_MESSAGE_TEXT)
                .build();
    }

    @Override
    public String getBotUsername() {

        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {

        return botConfig.getBotToken();
    }

}
