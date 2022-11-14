package org.bot.application;

import jakarta.mail.MessagingException;
import org.bot.domain.*;
import org.bot.enums.MessagesTemplates;
import org.bot.enums.UserState;
import org.bot.exceptions.SessionTimeExpiredException;
import org.bot.infrastructure.JakartaMailInterface;


public class BotLogic {
    private final StateMachine stateMachine = new StateMachine();
    private final UserRepository userRepository = new UserRepository();
    private final JakartaMailInterface mailInterface = new JakartaMailInterface();

    public Message getStartMessage() { // Выдаёт стартовое сообщение.
        Message message = new Message(MessagesTemplates.START_MESSAGE.text);
        return message;
    }

    public Message createResponse(Message message) {
        Integer userID = message.getUserID();
        User user = userRepository.getUserById(userID);

        String[] commands = message.getText().split(" ");
        UserState userState = stateMachine.getUserState(userID);

        switch (userState) { // проверяет текущее состоание пользователя, от которого зависит результат вызова команды
            case BASE_STATE -> {
                return baseStateHandler(message, user, commands);
            }
            case NOT_AUTHED -> {
                return notAuthedHandler(message, user, commands);
            }
            case WAITING_FOR_EMAIL -> {
                return waitingForEmailHandler(message, user, commands);
            }
            case WAITING_FOR_PASSWORD -> {
                return waitingForPasswordHandler(message, user, commands);
            }
            case SENDING -> {
                return emailSendingHandler(message, user, commands);
            }
            default -> { // TODO
                stateMachine.setUserState(userID, UserState.NOT_AUTHED);
                return new Message(MessagesTemplates.ERROR_MESSAGE.text);
            }
        }
    }

    private Message baseStateHandler(Message message, User user, String[] commands) {
        switch (commands[0]) {
            case "/auth":
                return new Message(MessagesTemplates.AUTH_UNAVAILABLE_MESSAGE.text);
            case "/help":
                return helpMessage();
            case "/send":
                stateMachine.setUserState(user.getID(), UserState.SENDING);
                return new Message(MessagesTemplates.SENDING_TEXT_MESSAGE.text);
            case "/list":
                int lettersCount = Integer.parseInt(commands[2]);
                return listOfMessages(user, commands[1], lettersCount);
            default:
                return new Message(MessagesTemplates.DEFAULT_MESSAGE.text);
        }
    }

    private Message waitingForEmailHandler(Message message, User user, String[] commands) {
        stateMachine.setUserState(user.getID(), UserState.WAITING_FOR_PASSWORD);
        user.setTempEmail(commands[0]);
        return new Message(MessagesTemplates.WAITING_FOR_PASSWORD.text);
    }

    private Message waitingForPasswordHandler(Message message, User user, String[] commands) {
        Mailbox mailbox = new Mailbox(user.getTempEmail(), commands[0]);
        if (!mailInterface.isCredentialsCorrect(mailbox)) {
            return new Message(MessagesTemplates.AUTH_ERROR_MESSAGE.text);
        }
        stateMachine.setUserState(user.getID(), UserState.BASE_STATE);
        user.addNewMailbox(mailbox);
        return new Message(MessagesTemplates.AUTH_SUCCESS_MESSAGE.text);
    }

    private Message notAuthedHandler(Message message, User user, String[] commands) {
        switch (commands[0]) {
            case "/help":
                return helpMessage();
            case "/auth":
                stateMachine.setUserState(user.getID(), UserState.WAITING_FOR_EMAIL);
                return new Message(MessagesTemplates.WAITING_FOR_EMAIL.text);
            case "/send":
                return new Message(MessagesTemplates.NOT_AUTH_SEND_IS_UNAVAILABLE.text);
            case "/list":
                return new Message(MessagesTemplates.NOT_AUTH_LIST_IS_UNAVAILABLE.text);
            default:
                return new Message(MessagesTemplates.DEFAULT_NOT_AUTH_MESSAGE.text);
        }
    }

    private Message emailSendingHandler(Message message, User user, String[] commands) {
        return new Message(MessagesTemplates.FUNCTION_NOT_AVAILABLE.text);
    }

    private Message listOfMessages(User user, String email, int lettersCont) {
        Mailbox mailbox = user.getMailbox(email);
        if (mailbox == null) {
            return new Message(MessagesTemplates.NOT_AUTH_LIST_IS_UNAVAILABLE.text);
        }
        Letter[] letters;
        try {
            letters = mailInterface.readMessages(mailbox, lettersCont);
        }
        catch (SessionTimeExpiredException e) {
            return new Message(MessagesTemplates.SESSION_EXPIRED.text);
        }
        catch (MessagingException e) {
            return new Message(MessagesTemplates.ERROR_MESSAGE.text);
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Letter letter : letters) {
            stringBuilder.append(letter.toString());
        }
        return new Message(stringBuilder.toString());
    }

    private Message helpMessage() {
        return new Message(MessagesTemplates.HELP_MESSAGE.text);
    }
}