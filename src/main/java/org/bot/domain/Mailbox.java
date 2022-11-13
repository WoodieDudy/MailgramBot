package org.bot.domain;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Mailbox {
    private String email;
    private String password;
    private LocalDateTime authTime;
    private final long sessionDuration = 1; // minutes

    public Mailbox(String email, String password) {
        this.email = email;
        this.password = password;
        this.authTime = LocalDateTime.now();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean ifSessionExpired(LocalDateTime currentDateTime) {
        return ChronoUnit.MINUTES.between(authTime, currentDateTime) > sessionDuration;
    }

    public void updateAuthTime(LocalDateTime time) {
        this.authTime = time;
    }
}