package org.bot.infrastructure.interfaces;

import jakarta.mail.MessagingException;
import org.bot.domain.Letter;
import org.bot.domain.Mailbox;
import org.bot.exceptions.SessionTimeExpiredException;


public interface MailInterface {
    default Letter[] readMessages(Mailbox mailbox, int lettersCount)
            throws MessagingException, SessionTimeExpiredException {
        if (mailbox.isSessionExpired()) {
            throw new SessionTimeExpiredException("Your session has expired. You should sign in again.");
        }
        return new Letter[0];
    }

    void sendMessage(Mailbox mailbox, Letter letter);

    boolean isCredentialsCorrect(Mailbox mailbox);
}
