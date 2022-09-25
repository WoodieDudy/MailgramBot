package org.bot.enums;

public enum UserState {
    SENDING("Sending"),
    NOT_AUTH("Not auth"),
    AUTH("Auth");

    public final String state;

    UserState(String state) {
        this.state = state;
    }
}