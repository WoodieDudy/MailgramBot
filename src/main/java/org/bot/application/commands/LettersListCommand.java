package org.bot.application.commands;

import jakarta.mail.MessagingException;
import org.bot.domain.Letter;
import org.bot.domain.Mailbox;
import org.bot.domain.Message;
import org.bot.domain.User;
import org.bot.enums.MessagesTemplates;
import org.bot.exceptions.SessionTimeExpiredException;
import org.bot.infrastructure.interfaces.MailInterface;

import java.util.ArrayList;
import java.util.List;

public class LettersListCommand extends Command {
    private final MailInterface mailInterface;

    record Args(String email, int lettersCount) {}

    private Args parseArgs(List<String> args) throws NumberFormatException {
        String email = args.get(0);
        int lettersCount = Integer.parseInt(args.get(1));
        return new Args(email, lettersCount);
    }

    public LettersListCommand(MailInterface mailInterface) {
        super(
            "letters",
            "<email> <n> - get n letters"
        );
        this.mailInterface = mailInterface;
    }

    public List<Message> execute(User user, List<String> args) {
        return baseStateHandler(user, args);
    }

    private List<Message> baseStateHandler(User user, List<String> rawArgs) {
        Args args;
        try {
            args = parseArgs(rawArgs);
        } catch (Exception e) {
            return List.of(new Message(MessagesTemplates.INCORRECT_ARGS.text, user.getId()));
        }
        Mailbox mailbox = user.getMailbox(args.email);

        if (mailbox == null) {
            return List.of(new Message(MessagesTemplates.NOT_AUTH_LIST_IS_UNAVAILABLE.text, user.getId()));
        }

        Letter[] letters;
        try {
            letters = this.mailInterface.readMessages(mailbox, args.lettersCount);
        } catch (SessionTimeExpiredException e) {
            return List.of(new Message(MessagesTemplates.SESSION_EXPIRED.text, user.getId()));
        } catch (MessagingException e) {
            return List.of(new Message(MessagesTemplates.ERROR_MESSAGE.text, user.getId()));
        }

        //TODO: Telegraph for full letters
        List<Message> lettersPreviews = new ArrayList<>();
        for (Letter letter : letters) {
            lettersPreviews.add(new Message(letter.asString(1000), user.getId()));
        }
        return lettersPreviews;
    }
}
