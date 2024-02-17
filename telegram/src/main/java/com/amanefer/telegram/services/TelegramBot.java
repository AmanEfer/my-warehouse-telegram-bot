package com.amanefer.telegram.services;

import com.amanefer.telegram.config.BotConfig;
import com.amanefer.telegram.dto.UserDto;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Setter
@Component
public class TelegramBot extends TelegramLongPollingBot {

    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    private final RestToCrud rest;
    private final BotConfig botConfig;
//    private String botToken;

//    public TelegramBot(String botToken, BotConfig botConfig) {
//        super(botToken);
//        this.botConfig = botConfig;
//    }

    public TelegramBot(BotConfig botConfig, RestToCrud rest) {
        this.botConfig = botConfig;
        this.rest = rest;

        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "start of application"));
        listOfCommands.add(new BotCommand("/register", "to register a new user"));

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

            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/register":
                case "register new user":
                    registerUser(update.getMessage());
                    break;
                case "get all users":
                    getAllUsers(update.getMessage());
                    break;
                case "get my data":
                    getCurrentUser(update.getMessage());
                    break;
                default:
                    sendMessage(chatId, "Sorry, command wasn't recognized");
            }
//        } else if (update.hasCallbackQuery()) {
//            String data = update.getCallbackQuery().getData();

        }
    }

    private void getCurrentUser(Message message) {
        UserDto user = rest.getUser(message.getFrom().getId());

        sendMessage(message.getChatId(), user.toString());
    }

    private void getAllUsers(Message message) {
        List<UserDto> users = rest.getUsers();

        String result = users.stream()
                .map(UserDto::getUsername)
                .collect(Collectors.joining("\n"));

        sendMessage(message.getChatId(), result);
    }

    private void registerUser(Message message) {
        long userId = message.getFrom().getId();
        String userName = message.getFrom().getUserName();

        UserDto user = new UserDto();
        user.setId(userId);
        user.setUsername(userName);

        user = rest.registerNewUser(user, ROLE_ADMIN);

//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setChatId(chatId);
//        sendMessage.setText("Choose your role");
//
//        InlineKeyboardMarkup inlineKeyboardMarkup = createInlineKeyboardMarkup();
//        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        sendMessage(message.getChatId(), String.format("User with username \'%s\' was registered", user.getUsername()));
    }

    private static InlineKeyboardMarkup createInlineKeyboardMarkup() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineRows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        var userButton = new InlineKeyboardButton();
        userButton.setText("User");
        userButton.setCallbackData(ROLE_USER);

        var adminButton = new InlineKeyboardButton();
        adminButton.setText("Admin");
        adminButton.setCallbackData(ROLE_ADMIN);

        row.add(userButton);
        row.add(adminButton);

        inlineRows.add(row);

        inlineKeyboardMarkup.setKeyboard(inlineRows);
        return inlineKeyboardMarkup;
    }

    private void startCommandReceived(long chatId, String firstName) {
        String answer = "Hi, " + firstName + ", nice to meet you!";
        log.info("Replied to user " + firstName);

        createReplyKeyboardMarkup(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);

        executeMessage(message);
    }

    private void createReplyKeyboardMarkup(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        row.add("register new user");

        keyboardRows.add(row);

        row = new KeyboardRow();

        row.add("get all users");
        row.add("get my data");

        keyboardRows.add(row);
        keyboardMarkup.setKeyboard(keyboardRows);
        message.setReplyMarkup(keyboardMarkup);

        executeMessage(message);
    }

    private void executeMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error occurred " + e.getMessage());
        }
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
