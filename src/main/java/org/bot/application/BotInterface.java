package org.bot.application;

import org.bot.domain.Message;

public interface BotInterface {
    public Message readMessage();

    public void sendMessage(Message message);
}