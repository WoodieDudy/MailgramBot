package org.bot.infrastructure.interfaces;

import jakarta.mail.MessagingException;
import org.bot.domain.Letter;
import org.bot.domain.Mailbox;

import java.io.IOException;


public interface MailInterface {
    public Letter[] readMessages(Mailbox mailbox, int lettersCount) throws MessagingException, IOException;

    public void sendMessage(Mailbox mailbox, Letter letter);
}
