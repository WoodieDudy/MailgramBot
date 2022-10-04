package org.bot;

import org.bot.application.BotLogic;
import org.bot.domain.Message;
import org.bot.enums.MessagesTemplates;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BotLogicTest {
    BotLogic botLogic = new BotLogic();

    @Test
    public void testGetStartMessage() {
        Message expectedMessage = new Message(MessagesTemplates.START_MESSAGE.text); // TODO параметризованные тесты
        Message receivedMessage = botLogic.getStartMessage();
        assertEquals(expectedMessage, receivedMessage);
    }

    @Test
    public void testNotAuthedHandlerForHelp() {
        Message expectedMessage = new Message(MessagesTemplates.HELP_MESSAGE.text);
        Message receivedMessage = botLogic.createResponse(new Message("/help", -1));
        assertEquals(expectedMessage, receivedMessage);
    }

    @Test
    public void testNotAuthedHandlerForList() {
        Message expectedMessage = new Message(MessagesTemplates.NOT_AUTH_LIST_IS_UNAVAILABLE.text);
        Message receivedMessage = botLogic.createResponse(new Message("/list", -1));
        assertEquals(expectedMessage, receivedMessage);
    }

    @Test
    public void testNotAuthedHandlerForSend() {
        Message expectedMessage = new Message(MessagesTemplates.NOT_AUTH_SEND_IS_UNAVAILABLE.text);
        Message receivedMessage = botLogic.createResponse(new Message("/send", -1));
        assertEquals(expectedMessage, receivedMessage);
    }

    @Test
    public void testNotAuthedHandlerForAuth() {
        Message expectedMessage = new Message(MessagesTemplates.WAITING_FOR_EMAIL.text);
        Message receivedMessage = botLogic.createResponse(new Message("/auth", -1));
        assertEquals(expectedMessage, receivedMessage);
    }

    @Test
    public void testWaitingForEmailHandlerCorrectInput() {
        botLogic.createResponse(new Message("/auth", -1));
        Message expectedMessage = new Message(MessagesTemplates.WAITING_FOR_PASSWORD.text);
        Message receivedMessage = botLogic.createResponse(new Message("example@gmail.com", -1));
        assertEquals(expectedMessage, receivedMessage);
    }

    @Test
    public void testWaitingForPasswordHandlerCorrectInput() {
        botLogic.createResponse(new Message("/auth", -1));
        botLogic.createResponse(new Message("example@gmail.com", -1));
        Message expectedMessage = new Message(MessagesTemplates.AUTH_SUCCESS_MESSAGE.text);
        Message receivedMessage = botLogic.createResponse(new Message("`123455678`", -1));
        assertEquals(expectedMessage, receivedMessage);
    }

    @Test
    public void testNotAuthedHandlerForWrongMessage() {
        Message expectedMessage = new Message(MessagesTemplates.DEFAULT_NOT_AUTH_MESSAGE.text);
        Message receivedMessage = botLogic.createResponse(new Message("/hlep", -1));
        assertEquals(expectedMessage, receivedMessage);
    }

    @Test
    public void testBaseStateHandlerForHelp() {
        botLogic.createResponse(new Message("/auth", -1));
        botLogic.createResponse(new Message("example@gmail.com", -1));
        botLogic.createResponse(new Message("qwerty12345", -1));
        Message expectedMessage = new Message(MessagesTemplates.HELP_MESSAGE.text);
        Message receivedMessage = botLogic.createResponse(new Message("/help", -1));
        assertEquals(expectedMessage, receivedMessage);
    }

    @Test
    public void testBaseStateHandlerForAuth() {
        botLogic.createResponse(new Message("/auth", -1));
        botLogic.createResponse(new Message("example@gmail.com", -1));
        botLogic.createResponse(new Message("qwerty12345", -1));
        Message expectedMessage = new Message(MessagesTemplates.AUTH_UNAVAILABLE_MESSAGE.text);
        Message receivedMessage = botLogic.createResponse(new Message("/auth", -1));
        assertEquals(expectedMessage, receivedMessage);
    }

    @Test
    public void testBaseStateHandlerForSend() {
        botLogic.createResponse(new Message("/auth", -1));
        botLogic.createResponse(new Message("example@gmail.com", -1));
        botLogic.createResponse(new Message("qwerty12345", -1));
        Message expectedMessage = new Message(MessagesTemplates.SENDING_TEXT_MESSAGE.text);
        Message receivedMessage = botLogic.createResponse(new Message("/send", -1));
        assertEquals(expectedMessage, receivedMessage);
    }

    @Test
    public void testBaseStateHandlerForList() {
        botLogic.createResponse(new Message("/auth", -1));
        botLogic.createResponse(new Message("example@gmail.com", -1));
        botLogic.createResponse(new Message("qwerty12345", -1));
        Message expectedMessage = new Message(MessagesTemplates.LIST_MESSAGE.text);
        Message receivedMessage = botLogic.createResponse(new Message("/list", -1));
        assertEquals(expectedMessage, receivedMessage);
    }

    @Test
    public void testBaseStateHandlerForWrongMessage() {
        botLogic.createResponse(new Message("/auth", -1));
        botLogic.createResponse(new Message("example@gmail.com", -1));
        botLogic.createResponse(new Message("qwerty12345", -1));
        Message expectedMessage = new Message(MessagesTemplates.DEFAULT_MESSAGE.text);
        Message receivedMessage = botLogic.createResponse(new Message("/hlep", -1));
        assertEquals(expectedMessage, receivedMessage);
    }

    @Test
    public void testEmailSendingHandler() {
        botLogic.createResponse(new Message("/auth", -1));
        botLogic.createResponse(new Message("example@gmail.com", -1));
        botLogic.createResponse(new Message("qwerty12345", -1));
        botLogic.createResponse(new Message("/send", -1));
        Message expectedMessage = new Message(MessagesTemplates.FUNCTION_NOT_AVAILABLE.text);
        Message receivedMessage = botLogic.createResponse(new Message("Привет!", -1));
        assertEquals(expectedMessage, receivedMessage);
    }
}
