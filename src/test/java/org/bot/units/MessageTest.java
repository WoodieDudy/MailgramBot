package org.bot.units;

import org.bot.domain.Message;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageTest {

    @Test
    public void testGetText() {
        String expectedText = "Hello, World!";
        Message message = new Message(expectedText, 1L);
        String actualText = message.getText();
        assertEquals(expectedText, actualText);
    }

    @Test
    public void testGetUserID() {
        Long expectedUserID = 1L;
        Message message = new Message("Hello, World!", expectedUserID);
        Long actualUserID = message.getUserID();
        assertEquals(expectedUserID, actualUserID);
    }

    @Test
    public void testGetMessageID() {
        Integer expectedMessageID = 1;
        Message message = new Message(expectedMessageID, 1L, new ArrayList<>());
        Integer actualMessageID = message.getMessageID();
        assertEquals(expectedMessageID, actualMessageID);
    }

    @Test
    public void testGetButtons() {
        List<InlineKeyboardButton> expectedButtons = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Test");
        button.setCallbackData("Test");
        expectedButtons.add(button);
        Message message = new Message("Hello, World!", 1L, expectedButtons);
        List<InlineKeyboardButton> actualButtons = message.getButtons();
        assertEquals(expectedButtons, actualButtons);
    }

    @Test
    public void testGetMarkup() {
        InlineKeyboardMarkup expectedMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Test");
        button.setCallbackData("Test");
        rowInline.add(button);
        rowsInline.add(rowInline);
        expectedMarkup.setKeyboard(rowsInline);
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(button);
        Message message = new Message("Hello, World!", 1L, buttons);
        InlineKeyboardMarkup actualMarkup = message.getMarkup();
        assertEquals(expectedMarkup, actualMarkup);
    }
}
