package org.bot.enums;

public enum UserState {
    NOT_AUTHED("Not authed"),
    WAITING_FOR_EMAIL("Waiting for email"),
    WAITING_FOR_PASSWORD("Waiting for password"),
    BASE_STATE("Base state"),
    SENDING("Sending");

    public final String state;

    UserState(String state) {
        this.state = state;
    }
}