package org.bot.application;

import org.bot.domain.Message;
import org.bot.domain.MessagesTemplates;

public class BotLogic {
    public Message getStartMessage() { // Выдаёт стартовое сообщение.
        Message message = new Message(MessagesTemplates.START_MESSAGE);
        return message;
    }
    public Message processCommandsMessage(Message message) { // Обрабатывает команды, получаемые от пользователя.
        // (переименован старый метод processMessages для разделения служебных команд и диалога при выполнении определённого сценария, задаваемого той или иной командой)
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
