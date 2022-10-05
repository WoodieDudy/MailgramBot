package org.bot.application;

import org.bot.domain.Message;

public final class Bot {
    private final BotInterface botInterface;
    private final BotLogic botLogic;

    public Bot(BotInterface botInterface, BotLogic botLogic) {
        this.botInterface = botInterface;
        this.botLogic = botLogic;
    }

    public void run() {
        Message startMessage = botLogic.getStartMessage();
        botInterface.sendMessage(startMessage);

        while (true) {
            Message message = botInterface.readMessage();
            Message response = botLogic.createResponse(message);
            botInterface.sendMessage(response);
        }
    }
}