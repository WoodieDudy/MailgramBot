package org.bot.infrastructure.interfaces;

import jakarta.mail.MessagingException;
import org.bot.domain.Letter;
import org.bot.domain.Mailbox;
import org.bot.exceptions.SessionTimeExpiredException;

import java.io.IOException;


public interface MailInterface {
    public Letter[] readMessages(Mailbox mailbox, int lettersCount) throws MessagingException, IOException, SessionTimeExpiredException;

    public void sendMessage(Mailbox mailbox, Letter letter);

    public boolean isCredentialsCorrect(Mailbox mailbox);
}
