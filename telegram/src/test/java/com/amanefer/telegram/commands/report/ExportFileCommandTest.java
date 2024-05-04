package com.amanefer.telegram.commands.report;

import com.amanefer.telegram.cache.UserStateCache;
import com.amanefer.telegram.services.RestToCrud;
import com.amanefer.telegram.util.Button;
import com.amanefer.telegram.util.UpdateTransferData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ExportFileCommandTest {

    @Mock
    private RestToCrud restToCrud;

    @Mock
    private UserStateCache userStateCache;

    @InjectMocks
    private ExportFileCommand command;

    private static final String PATH = "src/test/resources/textExample.csv";


    @Test
    void exportFileCommand_supportTest() {

        assertTrue(command.support(Button.EXPORT_FILE_BUTTON.toString()));
    }

    @Test
    void exportFileCommand_processTest_checkReturnedBytesArray() {

        byte[] expectedBytes;

        try {
            expectedBytes = Files.readAllBytes(Path.of(PATH));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Mockito.when(restToCrud.exportFile()).thenReturn(expectedBytes);

        UpdateTransferData data = UpdateTransferData.builder()
                .chatId(111111)
                .userId(222222)
                .build();

        SendDocument actual = command.process(data);

        byte[] actualBytes;

        try {
            actualBytes = actual.getDocument().getNewMediaStream().readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertArrayEquals(expectedBytes, actualBytes);
    }
}