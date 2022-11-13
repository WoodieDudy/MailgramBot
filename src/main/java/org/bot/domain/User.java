package org.bot.domain;

import org.bot.exceptions.EmailNotFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;

public class User {
    private Integer id;
    private HashMap<String, Mailbox> mailboxes;

    public User(Integer id) {
        this.id = id;
        mailboxes = new HashMap<>();
    }

    public Integer getID() {
        return id;
    }

    public void addNewMailbox(String email, String password) {
        if (mailboxes.containsKey(email)) {
            mailboxes.get(email).updateAuthTime(LocalDateTime.now());
        }
        Mailbox mailbox = new Mailbox(email, password);
    }

    public void removeMailBox(String email) throws EmailNotFoundException {
        if (mailboxes.containsKey(email)) {
            mailboxes.remove(email);
        }
        throw new EmailNotFoundException("Email not found.");
    }
}
