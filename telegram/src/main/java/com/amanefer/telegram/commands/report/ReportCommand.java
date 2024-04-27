package com.amanefer.telegram.commands.report;

import com.amanefer.telegram.util.Button;
import com.amanefer.telegram.cache.UserStateCache;
import com.amanefer.telegram.commands.Command;
import com.amanefer.telegram.util.UserState;
import com.amanefer.telegram.util.UpdateTransferData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReportCommand implements Command {

    public static final String MESSAGE_TEXT = "You can download some file";

    private final UserStateCache userStateCache;


    @Override
    public boolean support(String command) {

        return command.equals(Button.REPORTS_BUTTON.getMenuName())
                || command.equals(Button.REPORTS_BUTTON.getKeyboardName());
    }

    @Override
    public PartialBotApiMethod<Message> process(UpdateTransferData updateTransferData) {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton exportFileButton = InlineKeyboardButton.builder()
                .text(Button.EXPORT_FILE_BUTTON.getKeyboardName())
                .callbackData(Button.EXPORT_FILE_BUTTON.toString())
                .build();

        row.add(exportFileButton);
        keyboard.add(row);
        inlineKeyboardMarkup.setKeyboard(keyboard);

        userStateCache.putInCache(updateTransferData.getUserId(), UserState.PRIMARY);

        return SendMessage.builder()
                .chatId(updateTransferData.getChatId())
                .text(MESSAGE_TEXT)
                .replyMarkup(inlineKeyboardMarkup)
                .build();
    }
}
