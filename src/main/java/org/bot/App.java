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
    public static void main(String[] args) {
        String configPath = args[0];
        new Thread(new Runnable() {
            public void run() {
                startWebApp(11111);
            }
        }).start();
        new Thread(new Runnable() {
            public void run() {
                startBot(configPath);
            }
        }).start();
    }

    private static void startBot(String configPath) {
        MailInterface mailInterface = new JakartaMailInterface();

        String token;
        String name;
        String webAppUrl;
        try {
            Properties properties = PropertyParser.parseProperties(configPath);
            token = properties.getProperty("bot.token");
            name = properties.getProperty("bot.name");
            webAppUrl = properties.getProperty("webapp.url");
        }
        catch (IOException e) {
            return;
        }
        Bot bot = new Bot(
            mailInterface,
            token,
            name,
            webAppUrl
        );
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private static void startWebApp(int port) {
        org.webapp.WebApp.startHost(port);
    }
}