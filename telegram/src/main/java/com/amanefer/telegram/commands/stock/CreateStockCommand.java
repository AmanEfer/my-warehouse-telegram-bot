package com.amanefer.telegram.commands.stock;

import com.amanefer.telegram.util.Button;
import com.amanefer.telegram.cache.UserStateCache;
import com.amanefer.telegram.commands.Command;
import com.amanefer.telegram.dto.StockDto;
import com.amanefer.telegram.services.RestToCrud;
import com.amanefer.telegram.util.UserState;
import com.amanefer.telegram.util.UpdateTransferData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class CreateStockCommand implements Command {

    public static final String INPUT_WAREHOUSE_NAME = "Please, input your new warehouse's name";
    public static final String WAREHOUSE_CREATE_MESSAGE = "Warehouse '%s' was created successfully!";
    public static final String STOCK_CREATE_FAIL = "Warehouse wasn't created, try again";

    private final RestToCrud rest;
    private final UserStateCache userStateCache;


    @Override
    public boolean support(String command) {

        return command.equals(Button.CREATE_NEW_STOCK.toString());
    }

    @Override
    public PartialBotApiMethod<Message> process(UpdateTransferData updateTransferData) {

        long chatId = updateTransferData.getChatId();
        long userId = updateTransferData.getUserId();
        UserState userState = userStateCache.getFromCache(userId);

        if (userState == UserState.PRIMARY) {
            userStateCache.putInCache(userId, UserState.CREATE_STOCK);

            return SendMessage.builder()
                    .chatId(chatId)
                    .text(INPUT_WAREHOUSE_NAME)
                    .build();

        } else if (userState == UserState.CREATE_STOCK) {
            String stockName = rest.saveNewStock(new StockDto(updateTransferData.getDataForDto())).getStockName();

            userStateCache.putInCache(chatId, UserState.PRIMARY);

            return SendMessage.builder()
                    .chatId(chatId)
                    .text(String.format(WAREHOUSE_CREATE_MESSAGE, stockName))
                    .build();

        } else {
            userStateCache.putInCache(userId, UserState.PRIMARY);

            return SendMessage.builder()
                    .chatId(chatId)
                    .text(STOCK_CREATE_FAIL)
                    .build();
        }
    }

}
