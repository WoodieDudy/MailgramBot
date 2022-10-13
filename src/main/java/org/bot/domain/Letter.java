package org.bot.domain;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.text.MessageFormat;

public class Letter {
    private String subject;
    private String body;
    private String sender;
    private String date;

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public String getSender() { // TODO filter cringe sender names
        return sender;
    }

    public String getDate() {
        return date;
    }

    public static Letter fromMessage(Message message) throws MessagingException, IOException {
        Letter letter = new Letter();
        letter.subject = message.getSubject();
        letter.body = message.getContent().toString();
        letter.sender = message.getFrom()[0].toString();
        letter.date = message.getSentDate().toString();
        return letter;
    }

    public String toString() {
        return MessageFormat.format(
            """
            Sender: {0}
            Subject: {1}
            Date: {2}
            
            """, // TODO add body (parse html)
            sender, subject, date
        );
    }
}
