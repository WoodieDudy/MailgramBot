package org.bot.domain.commands;

import org.bot.domain.Message;
import org.bot.domain.User;
import org.bot.infrastructure.interfaces.MailInterface;

public class Command {
    protected final MailInterface mailInterface;
    String alias;
    String description; // TODO: add description

    public Command(MailInterface mailInterface) {
        this.mailInterface = mailInterface;
    }

    public Message execute(User user, String[] args) {
        return null;
    }

}
