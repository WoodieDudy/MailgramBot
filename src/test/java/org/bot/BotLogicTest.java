package org.bot;

import org.bot.domain.Letter;
import org.bot.domain.Mailbox;
import org.bot.domain.Message;
import org.bot.domain.User;
import org.bot.application.commands.AuthCommand;
import org.bot.application.commands.Command;
import org.bot.application.commands.LettersListCommand;
import org.bot.enums.MessagesTemplates;
import org.bot.infrastructure.interfaces.MailInterface;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class BotLogicTest {
    static class DummyMailInterface implements MailInterface {
        @Override
        public void sendMessage(Mailbox mailbox, Letter letter) {
        }

        @Override
        public boolean isCredentialsCorrect(Mailbox mailbox) {
            return mailbox.getEmail().equals("johndoe@test.com") && mailbox.getPassword().equals("password");
        }
    }

    MailInterface mailInterface = new DummyMailInterface();

    @Test
    public void testGetLettersCommandNonAuthed() {
        User user = new User(1L);
        Command lettersListCommand = new LettersListCommand(mailInterface);
        List<String> args = Arrays.asList("johndoe@test.com", "3");
        Message message = lettersListCommand.execute(user, args).get(0);

        String expectedResult = MessagesTemplates.NOT_AUTH_LIST_IS_UNAVAILABLE.text;
        String actualResult = message.getText();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testAuthCommandNotValid() {
        User user = new User(1L);
        Command auth = new AuthCommand(mailInterface, Duration.ofSeconds(1));
        List<String> args = Arrays.asList("johndoe@test.com", "wrongpassword");
        Message message = auth.execute(user, args).get(0);

        String expectedResult = MessagesTemplates.AUTH_ERROR_MESSAGE.text;
        String actualResult = message.getText();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testAuthCommandValid() {
        User user = new User(1L);
        Command auth = new AuthCommand(mailInterface, Duration.ofSeconds(1));
        List<String> args = Arrays.asList("johndoe@test.com", "password");
        Message message = auth.execute(user, args).get(0);

        String expectedResult = MessagesTemplates.AUTH_SUCCESS_MESSAGE.text;
        String actualResult = message.getText();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetLettersCommandAuthed() {
        User user = new User(1L);
        Command lettersListCommand = new LettersListCommand(mailInterface);
        List<String> args = Arrays.asList("johndoe@test.com", "password");
        Message message = lettersListCommand.execute(user, args).get(0);

        String expectedResult = MessagesTemplates.NOT_AUTH_LIST_IS_UNAVAILABLE.text;
        String actualResult = message.getText();
        assertNotEquals(expectedResult, actualResult);
    }

    @Test
    public void testSessionExpired() throws InterruptedException {
        User user = new User(1L);
        Command auth = new AuthCommand(mailInterface, Duration.ofSeconds(0));
        List<String> args = Arrays.asList("johndoe@test.com", "password");
        auth.execute(user, args);

        Command lettersListCommand = new LettersListCommand(mailInterface);
        TimeUnit.SECONDS.sleep(1);
        args = Arrays.asList("johndoe@test.com", "1");
        Message message = lettersListCommand.execute(user, args).get(0);

        String expectedResult = MessagesTemplates.SESSION_EXPIRED.text;
        String actualResult = message.getText();
        assertEquals(expectedResult, actualResult);
    }
}
