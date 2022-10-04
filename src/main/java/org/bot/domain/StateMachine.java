package org.bot.domain;

import org.bot.enums.UserState;

import java.util.HashMap;

public class StateMachine {
    private final HashMap<Integer, UserState> stateMachine;

    public StateMachine() {
        stateMachine = new HashMap<Integer, UserState>();
    }

    public void setUserState(Integer userID, UserState userState) { // TODO переход только в определенные стейты
        stateMachine.put(userID, userState);
    }

    public UserState getUserState(Integer userID) {
        if (!stateMachine.containsKey(userID)) {
            stateMachine.put(userID, UserState.NOT_AUTHED);
        }
        return stateMachine.get(userID);
    }
}