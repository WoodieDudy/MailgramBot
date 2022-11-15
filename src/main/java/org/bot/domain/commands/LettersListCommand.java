package org.bot.domain.commands;

import jakarta.mail.MessagingException;
import org.bot.domain.Letter;
import org.bot.domain.Mailbox;
import org.bot.domain.Message;
import org.bot.domain.User;
import org.bot.enums.MessagesTemplates;
import org.bot.exceptions.SessionTimeExpiredException;
import org.bot.infrastructure.interfaces.MailInterface;

public class LettersListCommand extends Command {
    private final MailInterface mailInterface;
    record Args(String email, int lettersCount) {}
    private Args parseArgs(String[] args) throws NumberFormatException {
        String email = args[0];
        int lettersCount = Integer.parseInt(args[1]);
        return new Args(email, lettersCount);
    }

    public LettersListCommand(MailInterface mailInterface) {
        super(
            "/letters",
            "<email> <n> - get n letters"
        );
        this.mailInterface = mailInterface;
    }

    public Message execute(User user, String[] args) {
        return baseStateHandler(user, args);
//        switch (user.getState()) {
//            case BASE_STATE -> {
//                return baseStateHandler(user, args);
//            }
//            default -> {
//                return new Message(MessagesTemplates.NOT_AUTH_LIST_IS_UNAVAILABLE.text);
//            }
//        }
    }

    private Message baseStateHandler(User user, String[] rawArgs) {
        Args args;
        try {
            args = parseArgs(rawArgs);
        }
        catch (Exception e) {
            return new Message(MessagesTemplates.INCORRECT_ARGS.text);
        }
        Mailbox mailbox = user.getMailbox(args.email);

        if (mailbox == null) {
            return new Message(MessagesTemplates.NOT_AUTH_LIST_IS_UNAVAILABLE.text);
        }

        Letter[] letters;
        try {
            letters = this.mailInterface.readMessages(mailbox, args.lettersCount);
        }
        catch (SessionTimeExpiredException e) {
            return new Message(MessagesTemplates.SESSION_EXPIRED.text);
        }
        catch (MessagingException e) {
            return new Message(MessagesTemplates.ERROR_MESSAGE.text);
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (Letter letter : letters) {
            stringBuilder.append(letter.toString());
        }
        return new Message(stringBuilder.toString());
    }
}
