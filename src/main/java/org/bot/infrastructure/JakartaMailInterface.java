package org.bot.infrastructure;

import com.sun.mail.imap.IMAPFolder;
import jakarta.mail.*;
import org.bot.domain.Letter;
import org.bot.domain.Mailbox;
import org.bot.exceptions.SessionTimeExpiredException;
import org.bot.infrastructure.interfaces.MailInterface;

import java.time.LocalDateTime;
import java.util.Properties;

public class JakartaMailInterface implements MailInterface {
    static private Properties getProperties(String email) {
        Properties properties = new Properties();

        properties.setProperty("mail.store.protocol", "imap");
        properties.setProperty("mail.imap.ssl.enable", "true");

        switch (email.split("@")[1]) {
            case "gmail.com" -> {
                properties.setProperty("mail.imap.host", "imap.gmail.com");
                properties.setProperty("mail.imap.port", "993");
            }
            case "yandex.ru" -> {
                properties.setProperty("mail.imap.host", "imap.yandex.ru"); // TODO change port and host
                properties.setProperty("mail.imap.port", "993");
            }
            default -> throw new IllegalArgumentException("Unknown email domain");
        }

        return properties;
    }

    @Override
    public Letter[] readMessages(Mailbox mailbox, int lettersCount)
            throws MessagingException, SessionTimeExpiredException {
        String email = mailbox.getEmail();
        String password = mailbox.getPassword();

        Properties props = getProperties(email);

        if (mailbox.ifSessionExpired(LocalDateTime.now())) {
            throw new SessionTimeExpiredException("Your session has expired. You should sign in again.");
        }

        Session session = Session.getDefaultInstance(props, null);

        Store store = session.getStore("imap");
        store.connect(props.getProperty("mail.imap.host"), email, password);
        IMAPFolder inbox = (IMAPFolder) store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);

        int totalMessages = inbox.getMessageCount();
        Message[] mailMessages = inbox.getMessages(totalMessages - lettersCount + 1, totalMessages);

        FetchProfile fp = new FetchProfile();
        fp.add(FetchProfile.Item.ENVELOPE);
        fp.add(IMAPFolder.FetchProfileItem.MESSAGE);

        inbox.fetch(mailMessages, fp);

        Letter[] letters = new Letter[mailMessages.length];
        for (int i = 0; i < mailMessages.length; i++) {
            letters[i] = Letter.fromMailMessage(mailMessages[i]);
        }
        return letters;
    }

    @Override
    public void sendMessage(Mailbox mailbox, Letter letter) {

    }

    @Override
    public boolean isCredentialsCorrect(Mailbox mailbox){
        try {
            Properties properties = getProperties(mailbox.getEmail());
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(mailbox.getEmail(), mailbox.getPassword());
                }
            });
            session.getStore().connect();
        } catch (MessagingException e) {
            return false;
        }
        return true;
    }
}
