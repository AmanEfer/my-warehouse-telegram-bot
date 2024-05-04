package com.amanefer.telegram.commands.report;

import com.amanefer.telegram.cache.UserStateCache;
import com.amanefer.telegram.commands.Command;
import com.amanefer.telegram.util.Button;
import com.amanefer.telegram.util.UpdateTransferData;
import com.amanefer.telegram.util.UserState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReportCommand implements Command {

    private static final InlineKeyboardMarkup REPORT_KEYBOARD;
    public static final String MESSAGE_TEXT = "You can download some file";

    private final UserStateCache userStateCache;

    static {
        InlineKeyboardButton exportFileButton = InlineKeyboardButton.builder()
                .text(Button.EXPORT_FILE_BUTTON.getKeyboardName())
                .callbackData(Button.EXPORT_FILE_BUTTON.toString())
                .build();

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(exportFileButton);

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(row);

        REPORT_KEYBOARD = new InlineKeyboardMarkup();
        REPORT_KEYBOARD.setKeyboard(keyboard);
    }


    public ReportCommand(UserStateCache userStateCache) {
        this.userStateCache = userStateCache;
    }

    @Override
    public boolean support(String command) {

        return command.equals(Button.REPORTS_BUTTON.getMenuName())
                || command.equals(Button.REPORTS_BUTTON.getKeyboardName());
    }

    @Override
    public PartialBotApiMethod<Message> process(UpdateTransferData updateTransferData) {

        userStateCache.putInCache(updateTransferData.getUserId(), UserState.PRIMARY);

        return SendMessage.builder()
                .chatId(updateTransferData.getChatId())
                .text(MESSAGE_TEXT)
                .replyMarkup(REPORT_KEYBOARD)
                .build();
    }
}
