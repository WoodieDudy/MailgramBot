package org.bot.application;

import org.bot.enums.UserState;

public interface DatabaseInterface {
    public UserState getUserState(Integer userId);
}
