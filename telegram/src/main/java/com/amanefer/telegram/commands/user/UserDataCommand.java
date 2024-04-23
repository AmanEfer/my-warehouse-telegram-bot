package com.amanefer.telegram.commands.user;

import com.amanefer.telegram.util.Button;
import com.amanefer.telegram.cache.UserStateCache;
import com.amanefer.telegram.commands.Command;
import com.amanefer.telegram.dto.UserDto;
import com.amanefer.telegram.services.RestToCrud;
import com.amanefer.telegram.util.BotState;
import com.amanefer.telegram.util.UpdateTransferData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@RequiredArgsConstructor
public class UserDataCommand implements Command {

    private final RestToCrud rest;
    private final UserStateCache userStateCache;


    @Override
    public boolean support(String command) {

        return command.equals(Button.GET_MY_DATA_BUTTON.toString());
    }

    @Override
    public SendMessage process(UpdateTransferData updateTransferData) {

        UserDto user = rest.getUser(updateTransferData.getUserId());

        userStateCache.putInCache(updateTransferData.getUserId(), BotState.PRIMARY);

        return SendMessage.builder()
                .chatId(updateTransferData.getChatId())
                .text(user.toString())
                .build();
    }

}
