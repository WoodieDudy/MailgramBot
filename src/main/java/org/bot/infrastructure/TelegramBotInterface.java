package org.bot.infrastructure;

import org.bot.domain.Message;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodSerializable;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.ArrayList;
import java.util.List;

public class TelegramBotInterface {
    public static SendMessage sendMessage(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getUserID());
        sendMessage.setText(message.getText());
        sendMessage.setReplyMarkup(message.getMarkup());
        return sendMessage;
    }

    public static List<BotApiMethodSerializable> editMessage(Message message) {
        List<BotApiMethodSerializable> editors = new ArrayList<>();

        if (message.getText() != null) {
            EditMessageText editText = new EditMessageText();
            editText.setText(message.getText());
            editText.setChatId(message.getUserID());
            editText.setMessageId(message.getMessageID());
            editors.add(editText);
        }

        if (message.getButtons() != null) {
            EditMessageReplyMarkup editReplyMarkup = new EditMessageReplyMarkup();
            editReplyMarkup.setReplyMarkup(message.getMarkup());
            editReplyMarkup.setChatId(message.getUserID());
            editReplyMarkup.setMessageId(message.getMessageID());
            editors.add(editReplyMarkup);
        }

        return editors;
    }
}
