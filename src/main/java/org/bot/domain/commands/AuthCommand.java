package org.bot.domain.commands;

import org.bot.domain.Mailbox;
import org.bot.domain.Message;
import org.bot.domain.User;
import org.bot.enums.MessagesTemplates;
import org.bot.infrastructure.interfaces.MailInterface;

public class AuthCommand extends Command {
    String alias = "/auth";
    String description = "/auth <email> <password> - authenticate to mailbox";
    record Args(String email, String password) {}
    private Args parseArgs(String[] args) {
        String email = args[0];
        String password = args[1];
        return new Args(email, password);
    }

    public AuthCommand(MailInterface mailInterface) {
        super(mailInterface);
    }

    public Message execute(User user, String[] args) {
        Args parsedArgs;
        try {
            parsedArgs = parseArgs(args);
        }
        catch (Exception e) {
            return new Message(MessagesTemplates.AUTH_INCORRECT_MESSAGE.text);
        }
        Mailbox mailbox = new Mailbox(parsedArgs.email, parsedArgs.password);
        if (this.mailInterface.isCredentialsCorrect(mailbox)) {
            user.addNewMailbox(mailbox);
            return new Message(MessagesTemplates.AUTH_SUCCESS_MESSAGE.text);
        }
        return new Message(MessagesTemplates.AUTH_ERROR_MESSAGE.text);
    }
}

