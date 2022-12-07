package org.bot.domain;


import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Message {
    private String text = null;
    private Integer messageId = null;
    private final Long userId;
    private List<InlineKeyboardButton> buttons = null;


    public String getText() {
        return text;
    }

    public Long getUserId() {
        return userId;
    }

    public Integer getMessageId() {
        return messageId;
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

    public Message(String text, Long userId) {
        this.text = text;
        this.userId = userId;
    }

    public Message(String text, Long userId, List<InlineKeyboardButton> buttons) {
        this.text = text;
        this.userId = userId;
        this.buttons = buttons;
    }

    public Message(Integer messageId, Long userId, List<InlineKeyboardButton> buttons) {
        this.messageId = messageId;
        this.userId = userId;
        this.buttons = buttons;
    }

    public Message(String text, Integer messageId, Long userId, List<InlineKeyboardButton> buttons) {
        this.text = text;
        this.messageId = messageId;
        this.userId = userId;
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
        return text.equals(message.text) && Objects.equals(userId, message.userId);
    }
}