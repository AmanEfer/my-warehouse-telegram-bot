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

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GettingAllStocksCommand implements Command {

    private final RestToCrud restToCrud;
    private final UserStateCache userStateCache;


    @Override
    public boolean support(String command) {

        return command.equals(Button.GET_ALL_STOCKS_BUTTON.toString());
    }

    @Override
    public PartialBotApiMethod<Message> process(UpdateTransferData updateTransferData) {

        String textMessage = "There aren't any saved stocks";
        List<StockDto> stocks = restToCrud.getAllStocks();

        if (!stocks.isEmpty())
            textMessage = stocks.stream()
                    .map(StockDto::getStockName)
                    .collect(Collectors.joining(",\n"));

        userStateCache.putInCache(updateTransferData.getUserId(), UserState.PRIMARY);

        return SendMessage.builder()
                .chatId(updateTransferData.getChatId())
                .text(textMessage)
                .build();
    }

}
