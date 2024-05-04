package com.amanefer.telegram.commands.report;

import com.amanefer.telegram.util.Button;
import com.amanefer.telegram.cache.UserStateCache;
import com.amanefer.telegram.commands.Command;
import com.amanefer.telegram.services.RestToCrud;
import com.amanefer.telegram.util.UserState;
import com.amanefer.telegram.util.UpdateTransferData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.ByteArrayInputStream;

@Component
@RequiredArgsConstructor
public class ExportFileCommand implements Command {

    public static final String FILE_NAME = "textExample.csv";

    private final RestToCrud rest;
    private final UserStateCache userStateCache;


    @Override
    public boolean support(String command) {

        return command.equals(Button.EXPORT_FILE_BUTTON.toString());
    }

    @Override
    public SendDocument process(UpdateTransferData updateTransferData) {

        byte[] fileAsBytes = rest.exportFile();

        InputFile inputFile = new InputFile(new ByteArrayInputStream(fileAsBytes), FILE_NAME);

        userStateCache.putInCache(updateTransferData.getUserId(), UserState.PRIMARY);

        return SendDocument.builder()
                .chatId(updateTransferData.getChatId())
                .document(inputFile)
                .build();
    }

}
