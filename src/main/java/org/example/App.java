package org.example;

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
