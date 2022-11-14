package org.bot.domain.commands;

import org.bot.domain.Message;
import org.bot.domain.User;
import org.bot.enums.MessagesTemplates;
import org.bot.infrastructure.interfaces.MailInterface;

public class HelpCommand extends Command {
    String alias = "/help";
    String description = "/help - get help";

    public HelpCommand(MailInterface mailInterface) {
        super(mailInterface); // TODO Auto-generated constructor stub
    }

    public Message execute(User user, String[] args) {
        return new Message(MessagesTemplates.HELP_MESSAGE.text);
    }
}
