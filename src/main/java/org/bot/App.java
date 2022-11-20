package org.bot;

import org.bot.application.Bot;
import org.bot.domain.commands.AuthCommand;
import org.bot.domain.commands.Command;
import org.bot.domain.commands.HelpCommand;
import org.bot.domain.commands.LettersListCommand;
import org.bot.infrastructure.JakartaMailInterface;
import org.bot.infrastructure.interfaces.BotInterface;
import org.bot.application.BotLogic;
import org.bot.infrastructure.ConsoleBotInterface;
import org.bot.infrastructure.interfaces.MailInterface;

import java.time.Duration;

public class App {
    public static void main(String[] args) {
        BotInterface botInterface = new ConsoleBotInterface();
        MailInterface mailInterface = new JakartaMailInterface();

        Command[] commands = createCommands(mailInterface);

        BotLogic botLogic = new BotLogic(commands);
        Bot bot = new Bot(botInterface, botLogic);
        bot.run();
    }

    private static Command[] createCommands(MailInterface mailInterface) { // TODO: send messages in commands
        HelpCommand helpCommand = new HelpCommand();
        Command[] commands = {
            new AuthCommand(mailInterface, Duration.ofDays(1)),
            new LettersListCommand(mailInterface),
            helpCommand
        };
        helpCommand.generateHelpMessage(commands);
        return commands;
    }
}