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

    public String getSender() {
        return sender;
    }

    public String getDate() {
        return date;
    }

    public static Letter fromMailMessage(Message message) {
        Letter letter = new Letter();
        letter.subject = parseSubject(message);
        letter.body = parseBody(message);
        letter.sender = parseSender(message);
        letter.date = parseDate(message);
        return letter;
    }

    private static String parseSubject(Message message) {
        try {
            return message.getSubject();
        } catch (MessagingException e) {
            return null;
        }
    }

    private static String parseSender(Message message) {
        try {
            Address[] froms = message.getFrom();
            return ((InternetAddress) froms[0]).getAddress();
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private static String parseBody(Message message) {
        try {
            String result = "";
            if (message.isMimeType("text/plain")) {
                result = message.getContent().toString();
            } else if (message.isMimeType("multipart/*")) {
                MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
                result = getTextFromMimeMultipart(mimeMultipart);
            } else if (message.isMimeType("text/html")) {
                String html = (String) message.getContent();
                result = Jsoup.parse(html).text();
            } else {
                System.out.println("Unknown message type " + message.getContentType());
            }
            result = result.replaceAll("[\r\n\s ]{2,}", "\n");
            result = result.replaceAll("[\t\s ]{2,}", "\s");
            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private static String parseDate(Message message) {
        try {
            return message.getSentDate().toString();
        } catch (MessagingException e) {
            return null;
        }
    }

    private static String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
        ArrayList<String> letterLines = new ArrayList<>();
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                String text = bodyPart.getContent().toString();
                letterLines.add(text);
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                String text = Jsoup.parse(html).text();
                letterLines.add(text);
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                letterLines.add(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
            }
        }
        return String.join("\n", letterLines);
    }


    public String asString(int maxLen) {
        String letterStr = MessageFormat.format(
                """
                        Отправитель: {0}
                        Тема: {1}
                        Дата: {2}
                                            
                        {3}""",
                sender, subject, date, body
        );
        return letterStr.substring(0, Math.min(letterStr.length(), maxLen)) + "\n...";
    }

    public String asString() {
        return MessageFormat.format(
                """
                        Отправитель: {0}
                        Тема: {1}
                        Дата: {2}
                                            
                        {3}""",
                sender, subject, date, body
        );
    }
}
