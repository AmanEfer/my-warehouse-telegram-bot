package com.amanefer.telegram.commands;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface Command {

    boolean support(String command);

    PartialBotApiMethod<Message> process(Message msg);
}
