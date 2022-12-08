package org.bot.domain;

import java.time.Duration;
import java.time.Instant;

public class Mailbox {
    private final String email;
    private final String password;
    private Instant expiresAt;
    private final Duration sessionDuration;

    public Mailbox(String email, String password, Duration sessionDuration) {
        this.email = email;
        this.password = password;
        this.sessionDuration = sessionDuration;
        this.expiresAt = Instant.now().plus(sessionDuration);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isSessionExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    public void updateAuthTime() {
        this.expiresAt = Instant.now().plus(sessionDuration);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Mailbox mailbox = (Mailbox) obj;
        return email.equals(mailbox.email);
    }
}