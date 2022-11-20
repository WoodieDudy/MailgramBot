package org.bot.application;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import org.bot.domain.Message;
import org.bot.infrastructure.interfaces.BotInterface;

import java.util.List;

public final class Bot {
    private final BotInterface botInterface;
    private final BotLogic botLogic;

    private TelegramBot telegramBot;

    public Bot(BotInterface botInterface, BotLogic botLogic) {
        this.botInterface = botInterface;
        this.botLogic = botLogic;
    }

    public Bot(BotInterface botInterface, BotLogic botLogic, TelegramBot telegramBot) {
        this.botInterface = botInterface;
        this.botLogic = botLogic;
        this.telegramBot = telegramBot;
    }

    public void run() { // TODO Вынести куда-то
        telegramBot.setUpdatesListener(new UpdatesListener() {
            @Override
            public int process(List<Update> updates) {
                for (Update update: updates) {
                    processUpdate(update);
                }
                return UpdatesListener.CONFIRMED_UPDATES_ALL;
            }
        });
    }

    private void processUpdate(Update update) {
        Message message = new Message(update);
        System.out.println(message.getText());
        Message response = botLogic.createResponse(message);
        System.out.println(response);
        botInterface.sendMessage(response);
    }
}