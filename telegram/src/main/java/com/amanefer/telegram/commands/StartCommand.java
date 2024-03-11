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

    private String ANSWER = "Hi, %s, nice to meet you!";

    @Override
    public boolean support(String command) {
        return command.equalsIgnoreCase("/start");
    }

    @Override
    public SendMessage process(Message msg) {
        long chatId = msg.getChatId();
        String firstName = msg.getChat().getUserName();

        String answer = String.format(ANSWER, firstName);

        return createReplyKeyboardMarkup(chatId, answer);
    }

    private SendMessage createReplyKeyboardMarkup(long chatId, String textToSend) {
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

        return message;
    }
}
