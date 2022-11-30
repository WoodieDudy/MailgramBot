package org.bot;

import org.bot.application.Bot;
import org.bot.infrastructure.JakartaMailInterface;
import org.bot.infrastructure.PropertyParser;
import org.bot.infrastructure.interfaces.MailInterface;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.util.Properties;


public class App {
    public static void main(String[] args) throws IOException {
        MailInterface mailInterface = new JakartaMailInterface();

        Properties properties = PropertyParser.getProperties("src/main/resources/config.properties");

        Bot bot = new Bot(
            mailInterface,
            properties.getProperty("bot.token"),
            properties.getProperty("bot.name")
        );
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}