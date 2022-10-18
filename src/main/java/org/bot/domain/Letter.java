package org.bot.domain;

import jakarta.mail.*;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMultipart;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;

public class Letter {
    private String subject;
    private String body;
    private String sender;
    private String date;
    // TODO mb have multipart with attachments

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
        letter.subject = parseSubject(message);
        letter.body = parseBody(message);
        letter.sender = parseSender(message);
        letter.date = parseDate(message);
        return letter;
    }

    private static String parseSubject(Message message) throws MessagingException {
        return message.getSubject();
    }

    private static String parseSender(Message message) throws MessagingException {
        Address[] froms = message.getFrom();
        return froms == null ? null : ((InternetAddress) froms[0]).getAddress();
    }

    private static String parseBody(Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        }
        else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        result = result.replaceAll("[\r\n\s ]{2,}", "\n");
        result = result.replaceAll("[\t\s ]{2,}", "\s");
        return result;
    }

    private static String parseDate(Message message) throws MessagingException {
        return message.getSentDate().toString();
    }

    private static String getTextFromMimeMultipart(MimeMultipart mimeMultipart)  throws MessagingException, IOException{
        ArrayList<String> letterLines = new ArrayList<>();
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                String text = bodyPart.getContent().toString();
                letterLines.add(text);
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                String text = Jsoup.parse(html).text();
                letterLines.add(text);
            } else if (bodyPart.getContent() instanceof MimeMultipart){
                letterLines.add(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
            }
        }
        return String.join("\n", letterLines);
    }

    public String toString() { // TODO про это говорили что-то мудрое на леции?
        return MessageFormat.format(
    """
            Sender: {0}
            Subject: {1}
            Date: {2}
            
            {3}""",
            sender, subject, date, body
        );
    }
}
