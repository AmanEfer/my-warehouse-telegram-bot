package com.amanefer.telegram.services;

import com.amanefer.telegram.commands.Command;
import com.amanefer.telegram.commands.TelegramCommands;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

import java.util.List;

@Slf4j
@Getter
@Setter
@Component
public class TelegramBot extends TelegramLongPollingBot {

    public static final String DEFAULT_MESSAGE_TEXT = "Sorry, command wasn't recognize";

    private final String botName;
    private final List<Command> commands;
    private final TelegramCommands telegramCommands;

    public TelegramBot(@Value("${bot.token}") String botToken,
                       @Value("${bot.name}") String botName,
                       List<Command> commands,
                       TelegramCommands telegramCommands) {

        super(botToken);
        this.botName = botName;
        this.commands = commands;
        this.telegramCommands = telegramCommands;
    }

    @PostConstruct
    public void init() {

        List<BotCommand> listOfCommands = telegramCommands.getListOfCommands();

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

        return botName;
    }

}
