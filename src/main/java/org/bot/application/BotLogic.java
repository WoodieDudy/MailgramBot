package org.bot.application;

import org.bot.domain.Message;
import org.bot.domain.StateMachine;
import org.bot.enums.MessagesTemplates;
import org.bot.enums.UserState;


public class BotLogic {

    private final StateMachine stateMachine = new StateMachine();

    public void setUserState(Integer userID, UserState userState) {
        stateMachine.setUserState(userID, userState);
    }

    public UserState GetUserState(Integer userID) {
        return stateMachine.getUserState(userID);
    }

    public Message getStartMessage() { // Выдаёт стартовое сообщение.
        Message message = new Message(MessagesTemplates.START_MESSAGE.text);
        return message;
    }

    public Message createResponse(Message message) {
        Integer userID = message.userID;
        String[] commands = message.text.split(" ");
        UserState userState = stateMachine.getUserState(userID);

        switch (userState) { // проверяет текущее состоание пользователя, от которого зависит результат вызова команды
            case BASE_STATE -> {
                return baseStateHandler(message, userID, commands);
            }
            case NOT_AUTHED -> {
                return notAuthedHandler(message, userID, commands);
            }
            case WAITING_FOR_EMAIL -> {
                return waitingForEmailHandler(message, userID, commands);
            }
            case WAITING_FOR_PASSWORD -> {
                return waitingForPasswordHandler(message, userID, commands);
            }
            case SENDING -> {
                return emailSendingHandler(message, userID, commands);
            }
            default -> { // TODO
                stateMachine.setUserState(userID, UserState.NOT_AUTHED);
                return new Message(MessagesTemplates.ERROR_MESSAGE.text);
            }
        }
    }

    private Message baseStateHandler(Message message, Integer userID, String[] commands) {
        switch (commands[0]) {
            case "/auth":
                stateMachine.setUserState(userID, UserState.WAITING_FOR_EMAIL);
                return new Message(MessagesTemplates.WAITING_FOR_EMAIL.text);

            case "/help":
                return helpMessage();

            case "/send":
                stateMachine.setUserState(userID, UserState.BASE_STATE); // TODO изменить на sending state
                return new Message(MessagesTemplates.SENDING_TEXT_MESSAGE.text);

            case "/list":
                return listOfMessages();

            default:
                return new Message(MessagesTemplates.DEFAULT_MESSAGE.text);
        }
    }

    private Message waitingForEmailHandler(Message message, Integer userID, String[] commands) {
        if (false) { // TODO проверка на валидность email
            return new Message(MessagesTemplates.MAIL_ERROR_MESSAGE.text);
        }

        stateMachine.setUserState(userID, UserState.WAITING_FOR_PASSWORD);
        return new Message(MessagesTemplates.WAITING_FOR_PASSWORD.text);
    }

    private Message waitingForPasswordHandler(Message message, Integer userID, String[] commands) {
        if (false) { // TODO проверка на валидность пароля
            return new Message(MessagesTemplates.AUTH_ERROR_MESSAGE.text);
        }
        stateMachine.setUserState(userID, UserState.BASE_STATE);
        return new Message(MessagesTemplates.AUTH_SUCCESS_MESSAGE.text);
    }

    private Message notAuthedHandler(Message message, Integer userID, String[] commands) {
        switch (commands[0]) {
            case "/help":
                return helpMessage();

            case "/auth":
                stateMachine.setUserState(userID, UserState.WAITING_FOR_EMAIL);
                return new Message(MessagesTemplates.WAITING_FOR_EMAIL.text);

            default:
                return new Message(MessagesTemplates.DEFAULT_NOT_AUTH_MESSAGE.text);
        }
    }

    private Message emailSendingHandler(Message message, Integer userID, String[] commands) {
        return new Message(MessagesTemplates.FUNCTION_NOT_AVAILABLE.text);
    }

    private Message listOfMessages() {
        return new Message(MessagesTemplates.LIST_MESSAGE.text);
    }

    private Message helpMessage() {
        return new Message(MessagesTemplates.HELP_MESSAGE.text);
    }

//    private Message userAuth(Integer userID, String login, String pass) {
//        stateMachine.setUserState(userID, UserState.BASE_STATE);
//        return new Message(MessagesTemplates.AUTH_SUCCESS_MESSAGE.text + "\n" + MessagesTemplates.FUNCTION_NOT_AVAILABLE.text);
//    }
}