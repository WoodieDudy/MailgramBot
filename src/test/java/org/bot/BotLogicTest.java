package org.bot;

import org.bot.application.BotLogic;
import org.bot.domain.Message;
import org.bot.enums.MessagesTemplates;
import org.bot.enums.UserState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BotLogicTest {
    BotLogic botLogic = new BotLogic();

    @Test
    public void testGetStartMessage() {
        Message expectedMessage = new Message(MessagesTemplates.START_MESSAGE.text);
        Message receivedMessage = botLogic.getStartMessage();
        assertEquals(expectedMessage, receivedMessage);
    }

    @Test
    public void testBaseStateHandlerForAuth() {
        botLogic.setUserState(-1, UserState.BASE_STATE);
        Message expectedMessage = new Message(MessagesTemplates.WAITING_FOR_EMAIL.text);
        Message receivedMessage = botLogic.createResponse(new Message("/auth", -1));
        assertEquals(expectedMessage, receivedMessage);
    }

    @Test
    public void testBaseStateHandlerForHelp() {
        botLogic.setUserState(-1, UserState.BASE_STATE);
        Message expectedMessage = new Message(MessagesTemplates.HELP_MESSAGE.text);
        Message receivedMessage = botLogic.createResponse(new Message("/help", -1));
        assertEquals(expectedMessage, receivedMessage);
    }

    @Test
    public void testBaseStateHandlerForSend() {
        botLogic.setUserState(-1, UserState.BASE_STATE);
        Message expectedMessage = new Message(MessagesTemplates.SENDING_TEXT_MESSAGE.text);
        Message receivedMessage = botLogic.createResponse(new Message("/send", -1));
        assertEquals(expectedMessage, receivedMessage);
    }

    @Test
    public void testBaseStateHandlerForList() {
        botLogic.setUserState(-1, UserState.BASE_STATE);
        Message expectedMessage = new Message(MessagesTemplates.LIST_MESSAGE.text);
        Message receivedMessage = botLogic.createResponse(new Message("/list", -1));
        assertEquals(expectedMessage, receivedMessage);
    }

    @Test
    public void testBaseStateHandlerForWrongMessage() {
        botLogic.setUserState(-1, UserState.BASE_STATE);
        Message expectedMessage = new Message(MessagesTemplates.DEFAULT_MESSAGE.text);
        Message receivedMessage = botLogic.createResponse(new Message("/hlep", -1));
        assertEquals(expectedMessage, receivedMessage);
    }

    @Test
    public void testNotAuthedHandlerForAuth() {
        botLogic.setUserState(-1, UserState.NOT_AUTHED);
        Message expectedMessage = new Message(MessagesTemplates.WAITING_FOR_EMAIL.text);
        Message receivedMessage = botLogic.createResponse(new Message("/auth", -1));
        assertEquals(expectedMessage, receivedMessage);
    }

    @Test
    public void testNotAuthedHandlerForHelp() {
        botLogic.setUserState(-1, UserState.NOT_AUTHED);
        Message expectedMessage = new Message(MessagesTemplates.HELP_MESSAGE.text);
        Message receivedMessage = botLogic.createResponse(new Message("/help", -1));
        assertEquals(expectedMessage, receivedMessage);
    }

    @Test
    public void testNotAuthedHandlerForWrongMessage() {
        botLogic.setUserState(-1, UserState.NOT_AUTHED);
        Message expectedMessage = new Message(MessagesTemplates.DEFAULT_NOT_AUTH_MESSAGE.text);
        Message receivedMessage = botLogic.createResponse(new Message("/send", -1));
        assertEquals(expectedMessage, receivedMessage);
    }

    @Test
    public void testWaitingForEmailHandlerCorrectInput() {
        botLogic.setUserState(-1, UserState.WAITING_FOR_EMAIL);
        Message expectedMessage = new Message(MessagesTemplates.WAITING_FOR_PASSWORD.text);
        Message receivedMessage = botLogic.createResponse(new Message("margaritayarmolchuk@gmail.com", -1));
        assertEquals(expectedMessage, receivedMessage);
    }

    @Test
    public void testWaitingForPasswordHandlerCorrectInput() {
        botLogic.setUserState(-1, UserState.WAITING_FOR_PASSWORD);
        Message expectedMessage = new Message(MessagesTemplates.AUTH_SUCCESS_MESSAGE.text);
        Message receivedMessage = botLogic.createResponse(new Message("`123455678`", -1));
        assertEquals(expectedMessage, receivedMessage);
    }

    @Test
    public void testEmailSendingHandler() {
        botLogic.setUserState(-1, UserState.SENDING);
        Message expectedMessage = new Message(MessagesTemplates.FUNCTION_NOT_AVAILABLE.text);
        Message receivedMessage = botLogic.createResponse(new Message("Привет!", -1));
        assertEquals(expectedMessage, receivedMessage);
    }
}
