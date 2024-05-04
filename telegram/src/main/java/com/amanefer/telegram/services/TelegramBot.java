package com.amanefer.telegram.services;

import com.amanefer.telegram.cache.ProductTransferDataCache;
import com.amanefer.telegram.cache.UserStateCache;
import com.amanefer.telegram.commands.Command;
import com.amanefer.telegram.commands.TelegramCommands;
import com.amanefer.telegram.util.UserState;
import com.amanefer.telegram.util.Button;
import com.amanefer.telegram.util.UpdateTransferData;
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
import java.util.stream.Stream;

@Slf4j
@Getter
@Setter
@Component
public class TelegramBot extends TelegramLongPollingBot {

    public static final String DEFAULT_MESSAGE_TEXT = "Sorry, that command doesn't exist";

    private final String botName;
    private final List<Command> commands;
    private final TelegramCommands telegramCommands;
    private final UserStateCache statesCache;
    private final ProductTransferDataCache productTransferDataCache;


    public TelegramBot(@Value("${bot.token}") String botToken,
                       @Value("${bot.name}") String botName,
                       List<Command> commands,
                       TelegramCommands telegramCommands,
                       UserStateCache userStateCache,
                       ProductTransferDataCache productTransferDataCache) {

        super(botToken);
        this.botName = botName;
        this.commands = commands;
        this.telegramCommands = telegramCommands;
        this.statesCache = userStateCache;
        this.productTransferDataCache = productTransferDataCache;
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
    public String getBotUsername() {

        return botName;
    }

    @Override
    public void onUpdateReceived(Update update) {

        UpdateTransferData updateTransferData = null;

        if (update.hasMessage() && update.getMessage().hasText()) {

            updateTransferData = createTransferUpdateData(
                    update.getMessage().getChatId(),
                    update.getMessage().getFrom().getId(),
                    update.getMessage().getText(),
                    update.getMessage().getFrom().getUserName()
            );

        } else if (update.hasCallbackQuery()) {

            updateTransferData = createTransferUpdateData(
                    update.getCallbackQuery().getMessage().getChatId(),
                    update.getCallbackQuery().getFrom().getId(),
                    update.getCallbackQuery().getData(),
                    update.getCallbackQuery().getFrom().getUserName()
            );
        }
        updateTransferData.setText(getMessageText(updateTransferData));

        PartialBotApiMethod<Message> message = getMessageForSending(updateTransferData);

        castAndExecuteSending(message);
    }

    private String getMessageText(UpdateTransferData updateTransferData) {

        long userId = updateTransferData.getUserId();
        String messageText = updateTransferData.getText();
        boolean isNotCommand = checkMessageTextIsNotCommand(messageText);
        UserState currentState = statesCache.getFromCache(updateTransferData.getUserId());

        switch (currentState) {

            case CREATE_STOCK -> {

                if (isNotCommand) {
                    updateTransferData.setDataForDto(messageText);
                    messageText = Button.CREATE_NEW_STOCK.toString();
                } else {
                    statesCache.putInCache(userId, UserState.PRIMARY);
                }
            }

            case SAVE_PRODUCT_INPUT_PRODUCT_TITLE,
                    SAVE_PRODUCT -> {

                if (isNotCommand) {
                    updateTransferData.setDataForDto(messageText);
                    messageText = Button.SAVE_NEW_PRODUCT.toString();
                } else {
                    statesCache.putInCache(userId, UserState.PRIMARY);
                }
            }

            case MOVE_PRODUCT_INPUT_INVOICE_NUMBER,
                    MOVE_PRODUCT_INPUT_STOCK_FROM,
                    MOVE_PRODUCT_INPUT_STOCK_TO,
                    MOVE_PRODUCT_INPUT_PRODUCT_ARTICLE,
                    MOVE_PRODUCT_INPUT_PRODUCT_QUANTITY -> {

                if (isNotCommand) {
                    updateTransferData.setDataForDto(messageText);
                    messageText = Button.MOVE_PRODUCTS_BUTTON.toString();
                } else {
                    statesCache.putInCache(userId, UserState.PRIMARY);
                }
            }
            case MOVE_PRODUCT -> {
                if (messageText.equals(Button.YES_MOVE_PRODUCT_BUTTON.toString())) {
                    String stockTo = productTransferDataCache.getFromCache(userId).getStockNameTo();
                    updateTransferData.setDataForDto(stockTo);
                    messageText = Button.MOVE_PRODUCTS_BUTTON.toString();

                    statesCache.putInCache(userId, UserState.MOVE_PRODUCT_INPUT_STOCK_TO);
                } else if (messageText.equals(Button.NO_MOVE_PRODUCT_BUTTON.toString())) {
                    messageText = Button.MOVE_PRODUCTS_BUTTON.toString();
                } else {
                    statesCache.putInCache(userId, UserState.PRIMARY);
                }
            }
        }

        return messageText;
    }

    private boolean checkMessageTextIsNotCommand(String messageText) {

        return Stream.of(Button.values())
                .flatMap(btn -> Stream.of(btn.name(), btn.getMenuName(), btn.getKeyboardName()))
                .noneMatch(t -> t.equals(messageText));
    }

    private PartialBotApiMethod<Message> getMessageForSending(UpdateTransferData updateTransferData) {

        return commands.stream()
                .filter(c -> c.support(updateTransferData.getText()))
                .findFirst()
                .map(c -> c.process(updateTransferData))
                .orElseGet(() -> getDefaultSendMessage(updateTransferData.getChatId()));
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

    private static UpdateTransferData createTransferUpdateData(long chatId, long userId, String text, String userName) {

        return UpdateTransferData.builder()
                .chatId(chatId)
                .userId(userId)
                .text(text)
                .userName(userName)
                .build();
    }

}
