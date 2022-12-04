package org.bot.domain;

import org.bot.enums.UserState;
import org.bot.exceptions.EmailNotFoundException;

import java.util.HashMap;
import java.util.List;

public class User {
    private final Long id;
    private UserState state;
    private final HashMap<String, Mailbox> mailboxes;
    private String tempEmail = null;

    public User(Long id) {
        this.id = id;
        mailboxes = new HashMap<>();
        state = UserState.NOT_AUTHED;
    }

    public Long getId() {
        return id;
    }

    public void addNewMailbox(Mailbox mailbox) {
        mailboxes.put(mailbox.getEmail(), mailbox);
    }

    public void removeMailBox(String email) throws EmailNotFoundException {
        if (mailboxes.containsKey(email)) {
            mailboxes.remove(email);
        }
        throw new EmailNotFoundException("Email not found.");
    }

    public Mailbox getMailbox(String email) {
        return mailboxes.get(email);
    }

    public List<String> getAllEmails() {
        return List.copyOf(mailboxes.keySet());
    }

    public UserState getState() {
        return state;
    }
    public void changeState(UserState state) {
        this.state = state;
    }

    public void setTempEmail(String email) {
        tempEmail = email;
    }
    public String getTempEmail() {
        return tempEmail;
    }
}
