package com.amanefer.telegram.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface Command {

    boolean support(String command);

    SendMessage process(Message msg);
}
