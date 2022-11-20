package org.bot.infrastructure;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.bot.domain.Message;
import org.bot.infrastructure.interfaces.BotInterface;

public class TelegramBotInterface implements BotInterface {

    private final TelegramBot bot;

    public TelegramBotInterface(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public Message readMessage() {
        return null;
    }

    @Override
    public void sendMessage(Message message) {
        bot.execute(new SendMessage(message.getUserID(), message.getText()));
    }
}
