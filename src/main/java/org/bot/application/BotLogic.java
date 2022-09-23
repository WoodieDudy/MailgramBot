package org.bot.application;

import org.bot.domain.Message;
import org.bot.domain.MessagesTemplates;

public class BotLogic {
    public Message getStartMessage() {
        Message message = new Message(MessagesTemplates.START_MESSAGE);
        return message;
    }
    public Message processMessage(Message message) {
        String text = message.text;
        switch (text) {
            case "/help":
                text = MessagesTemplates.HELP_MESSAGE;
                break;
            default:
                text = MessagesTemplates.DEFAULT_MESSAGE;
        }
        return new Message(text);
    }
}
