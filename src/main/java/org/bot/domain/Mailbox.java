package org.bot.domain;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Mailbox {
    private String email;
    private String password;
    private LocalDateTime authTime;
    private long sessionDurationSeconds;

    public Mailbox(String email, String password, long sessionDurationSeconds) {
        this.email = email;
        this.password = password;
        this.authTime = LocalDateTime.now();
        this.sessionDurationSeconds = sessionDurationSeconds;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean ifSessionExpired(LocalDateTime currentDateTime) {
        return ChronoUnit.SECONDS.between(authTime, currentDateTime) > sessionDurationSeconds;
    }

    public void updateAuthTime(LocalDateTime time) {
        this.authTime = time;
    }
}