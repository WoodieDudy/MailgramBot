package org.bot.domain;


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
}