package org.bot.domain;


import java.util.Objects;

public class Message {
    public String text;
    public Integer userID;

    public Message(String text) {
        this.text = text;
    }

    public Message(String text, Integer userID) {
        this.text = text;
        this.userID = userID;
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