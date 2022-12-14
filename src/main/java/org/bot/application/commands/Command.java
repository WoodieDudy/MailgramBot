package org.bot.application.commands;

import org.bot.domain.Message;
import org.bot.domain.User;

import java.util.List;

abstract public class Command {
    private final String alias;
    private final String description;

    public Command(String alias, String description) {
        this.alias = alias;
        this.description = description;
    }

    public String getAlias() {
        return alias;
    }

    public String getDescription() {
        return description;
    }

    public abstract List<Message> execute(User user, List<String> args);
}
