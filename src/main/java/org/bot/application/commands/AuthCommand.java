package org.bot.application.commands;

import org.bot.domain.Mailbox;
import org.bot.domain.Message;
import org.bot.domain.User;
import org.bot.enums.MessagesTemplates;
import org.bot.infrastructure.interfaces.MailInterface;

import java.time.Duration;
import java.util.List;

public class AuthCommand extends Command {
    private final MailInterface mailInterface;
    private final Duration sessionDuration;

    record Args(String email, String password) {
    }

    private Args parseArgs(List<String> args) {
        if (args.size() != 2) {
            throw new IllegalArgumentException("Wrong args number");
        }
        String email = args.get(0);
        String password = args.get(1);
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Invalid email");
        }
        return new Args(email, password);
    }

    public AuthCommand(MailInterface mailInterface, Duration sessionDuration) {
        super(
                "auth",
                "<email> <password> - authenticate to mailbox"
        );
        this.mailInterface = mailInterface;
        this.sessionDuration = sessionDuration;
    }

    public List<Message> execute(User user, List<String> args) {
        Args parsedArgs;
        try {
            parsedArgs = parseArgs(args);
        } catch (Exception e) {
            return List.of(new Message(MessagesTemplates.AUTH_INCORRECT_MESSAGE.text, user.getId()));
        }
        Mailbox mailbox = new Mailbox(parsedArgs.email(), parsedArgs.password(), sessionDuration);
        if (this.mailInterface.isCredentialsCorrect(mailbox)) {
            user.addNewMailbox(mailbox);
            return List.of(new Message(MessagesTemplates.AUTH_SUCCESS_MESSAGE.text, user.getId()));
        }
        return List.of(new Message(MessagesTemplates.AUTH_ERROR_MESSAGE.text, user.getId()));
    }
}

