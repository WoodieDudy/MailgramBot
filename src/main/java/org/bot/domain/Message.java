package org.bot.domain;


import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Message {
    private String text = null;
    private Integer messageID = null;
    private final Long userID;
    private List<InlineKeyboardButton> buttons = null;


    public String getText() {
        return text;
    }

    public Long getUserID() {
        return userID;
    }
    public Integer getMessageID() {
        return messageID;
    }
    public List<InlineKeyboardButton> getButtons() {
        return buttons;
    }

    public InlineKeyboardMarkup getMarkup() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        if (buttons != null) {
            for (InlineKeyboardButton button : buttons) {
                List<InlineKeyboardButton> rowInline = new ArrayList<>();
                rowInline.add(button);
                rowsInline.add(rowInline);
            }
        }

        inlineKeyboardMarkup.setKeyboard(rowsInline);
        return inlineKeyboardMarkup;
    }

    public Message(String text, Long userID) {
        this.text = text;
        this.userID = userID;
    }

    // for sendMessage from BotInterface
    public Message(String text, Long userID, List<InlineKeyboardButton> buttons) {
        this.text = text;
        this.userID = userID;
        this.buttons = buttons;
    }

    // for editMessage from BotInterface
    public Message(Integer messageID, Long userID, List<InlineKeyboardButton> buttons) {
        this.messageID = messageID;
        this.userID = userID;
        this.buttons = buttons;
    }
    public Message(String text, Integer messageID, Long userID, List<InlineKeyboardButton> buttons) {
        this.text = text;
        this.messageID = messageID;
        this.userID = userID;
        this.buttons = buttons;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null || object.getClass() != this.getClass()) {
            return false;
        }
        Message message = (Message) object;
        return this.text.equals(message.text) && Objects.equals(this.userID, message.userID);
    }
}