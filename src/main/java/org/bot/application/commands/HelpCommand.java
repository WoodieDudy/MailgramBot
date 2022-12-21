package org.bot.application.commands;

import org.bot.domain.Message;
import org.bot.domain.User;

import java.util.List;

public class HelpCommand extends Command {
    String helpMessage;

    public HelpCommand() {
        super(
            "help",
            "- для получения справки по командам"
        );
    }

    public void generateHelpMessage(Command[] commands) {
        StringBuilder helpMessageBuilder = new StringBuilder();
        for (Command command : commands) {
            helpMessageBuilder.append("/");
            helpMessageBuilder.append(command.getAlias());
            helpMessageBuilder.append(" ");
            helpMessageBuilder.append(command.getDescription());
            helpMessageBuilder.append("\n");
        }
        helpMessage = helpMessageBuilder.toString();
    }

    public List<Message> execute(User user, List<String> args) {
        return List.of(new Message(helpMessage, user.getId()));
    }
}
