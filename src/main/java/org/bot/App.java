package org.bot;

import org.bot.application.Bot;
import org.bot.infrastructure.JakartaMailInterface;
import org.bot.infrastructure.interfaces.MailInterface;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


public class App {
    public static void main(String[] args) {
        MailInterface mailInterface = new JakartaMailInterface();

        Bot bot = new Bot(
            mailInterface,
            "",
            ""
        );
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}