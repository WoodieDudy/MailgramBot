package org.bot.infrastructure;

import org.bot.domain.User;
import org.bot.enums.MessagesTemplates;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodSerializable;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TelegramBotInterface {

    public static List<SendMessage> sendMessageList(List<org.bot.domain.Message> messages) {
        List<SendMessage> sendMessages = new ArrayList<>();
        for (org.bot.domain.Message message : messages) {
            sendMessages.add(sendMessage(message));
        }
        return sendMessages;
    }

    public static SendMessage sendMessage(org.bot.domain.Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getUserId());
        sendMessage.setText(message.getText());
        sendMessage.setReplyMarkup(message.getMarkup());
        return sendMessage;
    }

    public static List<BotApiMethodSerializable> editMessage(org.bot.domain.Message message) {
        List<BotApiMethodSerializable> editors = new ArrayList<>();

        if (message.getText() != null) {
            EditMessageText editText = new EditMessageText();
            editText.setText(message.getText());
            editText.setChatId(message.getUserId());
            editText.setMessageId(message.getMessageId());
            editors.add(editText);
        }

        if (message.getButtons() != null) {
            EditMessageReplyMarkup editReplyMarkup = new EditMessageReplyMarkup();
            editReplyMarkup.setReplyMarkup(message.getMarkup());
            editReplyMarkup.setChatId(message.getUserId());
            editReplyMarkup.setMessageId(message.getMessageId());
            editors.add(editReplyMarkup);
        }

        return editors;
    }

    public static List<InlineKeyboardButton> numberOfLetters(String fromMessageId) {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        for (int lettersNumber : new int[]{1, 2, 4}) {
            InlineKeyboardButton button = new InlineKeyboardButton(String.valueOf(lettersNumber));
            button.setCallbackData("chooseNum " + fromMessageId + " " + lettersNumber);
            buttons.add(button);
        }
        return buttons;
    }

    public static SendMessage lettersAliasEmailWithoutKeyboard(User user, List<String> emails) {
        if (emails.isEmpty()) {
            return sendMessage(new org.bot.domain.Message(
                    MessagesTemplates.NOT_AUTH_LIST_IS_UNAVAILABLE.text,
                    user.getId()));
        }
        org.bot.domain.Message message = new org.bot.domain.Message(
                MessagesTemplates.CHOOSE_EMAIL.text,
                user.getId()
        );

        return sendMessage(message);
    }

    public static List<BotApiMethodSerializable> lettersAliasEmailWithKeyboard(
            User user, List<String> emails, Optional<Message> sentMessage) {
        Integer sentMessageId = sentMessage.get().getMessageId();


        List<InlineKeyboardButton> buttons = new ArrayList<>();
        for (String email : emails) {
            InlineKeyboardButton button = new InlineKeyboardButton(email);
            button.setCallbackData("letters " + sentMessageId + " " + email);
            buttons.add(button);
        }

        org.bot.domain.Message messageToEdit = new org.bot.domain.Message(
                sentMessageId,
                user.getId(),
                buttons
        );

        return editMessage(messageToEdit);
    }
}
