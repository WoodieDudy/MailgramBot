package org.bot.application;

import org.bot.domain.Message;
import org.bot.domain.StateMachine;
import org.bot.enums.MessagesTemplates;
import org.bot.enums.UserState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BotLogic {

    private StateMachine machine = new StateMachine();

    public Message getStartMessage() { // Выдаёт стартовое сообщение.
        Message message = new Message(MessagesTemplates.START_MESSAGE.text);
        return message;
    }

    public Message createResponse(Message message) {
        Integer currentUserID = message.userID;
        UserState userState = machine.getUserState(currentUserID);
        String currCommand = message.text.split(" ")[0];
        switch (userState) { // проверяет текущее состоание пользователя, от которого зависит результат вызова команды
            case AUTH -> {
                switch (currCommand) {
                    case "/auth":
                        return new Message(MessagesTemplates.AUTH_UNAVAILABLE_MESSAGE.text);
                    case "/help":
                        return helpMessage();
                    case "/send":
                        machine.setUserState(currentUserID, UserState.SENDING);
                        return new Message(MessagesTemplates.SENDING_TEXT_MESSAGE.text);
                    case "/list":
                        return listOfMessages();
                    default:
                        return new Message(MessagesTemplates.DEFAULT_MESSAGE.text);
                }
            }
            case SENDING -> {
                machine.setUserState(currentUserID, UserState.AUTH);
                return sendingFunctional();
            }
            case NOT_AUTH -> {
                switch (currCommand) {
                    case "/help":
                        return helpMessage();
                    case "/auth":
                        ArrayList<String> splittedMsg = new ArrayList<>(List.of(message.text.split(" ")));
                        if (splittedMsg.size() != 3) {
                            return new Message(MessagesTemplates.AUTH_INCORRECT_MESSAGE.text);
                        }
                        return userAuth(message.userID, message.text.split(" ")[1], message.text.split(" ")[2]);
                    default:
                        return new Message(MessagesTemplates.DEFAULT_NOT_AUTH_MESSAGE.text);
                }
            }
        }
        return null;
    }

    private Message userAuth(Integer userID, String login, String pass) {
        machine.setUserState(userID, UserState.AUTH);
        return new Message(MessagesTemplates.AUTH_SUCCESS_MESSAGE.text + "\n" + MessagesTemplates.FUNCTION_NOT_AVAILABLE.text);
    }


    private Message sendingFunctional() {
        return new Message(MessagesTemplates.FUNCTION_NOT_AVAILABLE.text);
    }

    private Message listOfMessages() {
        return new Message(MessagesTemplates.LIST_MESSAGE.text);
    }

    private Message helpMessage() {
        return new Message(MessagesTemplates.HELP_MESSAGE.text);
    }
}