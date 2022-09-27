package org.bot.domain;

import org.bot.enums.UserState;

import javax.swing.plaf.nimbus.State;
import java.util.HashMap;
import java.util.Map;

public class StateMachine {
    private Map<Integer, UserState> stateMachine;

    public StateMachine() {
        stateMachine = new HashMap<Integer, UserState>();
    }

    public void setUserState(Integer userID, UserState userState) {
        stateMachine.put(userID, userState);
    }

    public UserState getUserState(Integer userID) {
        if (!stateMachine.containsKey(userID)) {
            stateMachine.put(userID, UserState.NOT_AUTH);
        }
        return stateMachine.get(userID);
    }
}