package com.amanefer.telegram.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class StartCommand implements Command {

    public static final String START_COMMAND = "/start";
    public static final String REGISTER_NEW_USER_COMMAND = "register new user";
    public static final String EXPORT_COMMAND = "export";
    public static final String GET_ALL_USERS_COMMAND = "get all users";
    public static final String GET_MY_DATA_COMMAND = "get my data";
    private static final String MESSAGE_TEXT = "Hi, %s, nice to meet you!";


    @Override
    public boolean support(String command) {

        return command.equalsIgnoreCase(START_COMMAND);
    }

    @Override
    public SendMessage process(Message msg) {

        long chatId = msg.getChatId();
        String firstName = msg.getChat().getUserName();

        String answer = String.format(MESSAGE_TEXT, firstName);

        return createReplyKeyboardMarkup(chatId, answer);
    }

    private SendMessage createReplyKeyboardMarkup(long chatId, String textToSend) {
        
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        row.add(REGISTER_NEW_USER_COMMAND);
        row.add(EXPORT_COMMAND);

        keyboardRows.add(row);

        row = new KeyboardRow();

        row.add(GET_ALL_USERS_COMMAND);
        row.add(GET_MY_DATA_COMMAND);

        keyboardRows.add(row);
        keyboardMarkup.setKeyboard(keyboardRows);
        message.setReplyMarkup(keyboardMarkup);

        return message;
    }
    
}
