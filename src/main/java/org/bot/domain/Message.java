package org.bot.domain;


import java.util.Objects;

public class Message {
    private final String text;
    private Long userID;

    public String getText() {
        return text;
    }

    public Long getUserID() {
        return userID;
    }

    public Message(String text, Long userID) {
        this.text = text;
        this.userID = userID;
    }

    public Message(Update update) {
        text = update.message().text();
        userID = update.message().chat().id();
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