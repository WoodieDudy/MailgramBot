package org.bot.domain;

import java.util.Date;

public class Mailbox {
    private String email;
    private String password;
    private Date expireTime;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Date getExpireTime() {
        return expireTime;
    }
}