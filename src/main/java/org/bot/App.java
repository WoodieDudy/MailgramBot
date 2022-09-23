package org.bot;

import org.bot.application.Bot;
import org.bot.application.BotInterface;
import org.bot.application.BotLogic;
import org.bot.infrastructure.ConsoleBotInterface;

public class App
{
    public static void main( String[] args )
    {
        BotInterface botInterface = new ConsoleBotInterface();
        BotLogic botLogic = new BotLogic();
        Bot bot = new Bot(botInterface, botLogic);
        bot.run();
    }
}
