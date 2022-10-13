package org.bot.infrastructure;

import com.sun.mail.imap.IMAPFolder;
import jakarta.mail.*;
import org.bot.domain.Letter;
import org.bot.domain.Mailbox;
import org.bot.infrastructure.interfaces.MailInterface;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public class JakartaMailInterface implements MailInterface {
    private Properties getProperties(String email) {
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
    public Letter[] readMessages(Mailbox mailbox, int lettersCount) throws MessagingException, IOException {
        String email = mailbox.getEmail();
        String password = mailbox.getPassword();

        Properties props = getProperties(email);

        if (mailbox.getExpireTime().getTime() < new Date().getTime()) {
            throw new IllegalArgumentException("Mailbox expired");
        }

        Session session = Session.getDefaultInstance(props, null);

        Store store = session.getStore("imap");
        store.connect(props.getProperty("mail.imap.host"), email, password);
        IMAPFolder inbox = (IMAPFolder) store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);

        int totalMessages = inbox.getMessageCount();
        Message[] messages = inbox.getMessages(totalMessages - lettersCount + 1, totalMessages);

        FetchProfile fp = new FetchProfile();
        fp.add(FetchProfile.Item.ENVELOPE);
        fp.add(IMAPFolder.FetchProfileItem.MESSAGE);

        inbox.fetch(messages, fp);

        Letter[] letters = new Letter[messages.length];
        for (int i = 0; i < messages.length; i++) {
            letters[i] = Letter.fromMessage(messages[i]);
        };
        return letters;
    }

    @Override
    public void sendMessage(Mailbox mailbox, Letter letter) {

    }
}
