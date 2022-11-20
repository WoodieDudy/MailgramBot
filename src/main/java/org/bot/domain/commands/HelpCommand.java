package org.bot.domain.commands;

import org.bot.domain.Message;
import org.bot.domain.User;

public class HelpCommand extends Command {
    String helpMessage;

    public HelpCommand() {
        super(
            "/help",
            "- get help"
        );
    }

    public void generateHelpMessage(Command[] commands) {
        StringBuilder helpMessageBuilder = new StringBuilder();
        for (Command command : commands) {
            helpMessageBuilder.append(command.getAlias());
            helpMessageBuilder.append(" ");
            helpMessageBuilder.append(command.getDescription());
            helpMessageBuilder.append("\n");
        }
        helpMessage = helpMessageBuilder.toString();
    }

    public Message execute(User user, String[] args) {
        return new Message(helpMessage);
    }
}
