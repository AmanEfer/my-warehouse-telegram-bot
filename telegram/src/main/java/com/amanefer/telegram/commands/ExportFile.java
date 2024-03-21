package com.amanefer.telegram.commands;

import com.amanefer.telegram.services.RestToCrud;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.ByteArrayInputStream;

@Component
@RequiredArgsConstructor
@Builder
public class ExportFile implements Command {

    public static final String EXPORT_COMMAND = "/?export";
    public static final String FILE_NAME = "textExample.csv";

    private final RestToCrud rest;


    @Override
    public boolean support(String command) {

        return command.toLowerCase().matches(EXPORT_COMMAND);
    }

    @Override
    public SendDocument process(Message msg) {

        byte[] fileAsBytes = rest.exportFile();

        InputFile inputFile = new InputFile(new ByteArrayInputStream(fileAsBytes), FILE_NAME);

        return SendDocument.builder()
                .chatId(msg.getChatId())
                .document(inputFile)
                .build();
    }

}
