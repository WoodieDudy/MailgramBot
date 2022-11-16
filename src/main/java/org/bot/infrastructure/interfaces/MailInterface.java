package org.bot.infrastructure.interfaces;

import jakarta.mail.MessagingException;
import org.bot.domain.Letter;
import org.bot.domain.Mailbox;
import org.bot.exceptions.SessionTimeExpiredException;

import java.io.IOException;
import java.time.LocalDateTime;


public interface MailInterface {
    public default Letter[] readMessages(Mailbox mailbox, int lettersCount)
            throws MessagingException, SessionTimeExpiredException {
        if (mailbox.ifSessionExpired(LocalDateTime.now())) {
            throw new SessionTimeExpiredException("Your session has expired. You should sign in again.");
        }
        return new Letter[0];
    }

    public void sendMessage(Mailbox mailbox, Letter letter);

    public boolean isCredentialsCorrect(Mailbox mailbox);
}
