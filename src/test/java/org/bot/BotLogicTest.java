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
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class BotLogicTest {
    static class DummyMailInterface implements MailInterface {
        @Override
        public void sendMessage(Mailbox mailbox, Letter letter) {
        }

        @Override
        public boolean isCredentialsCorrect(Mailbox mailbox) {
            return mailbox.getEmail().equals("test@test.com") && mailbox.getPassword().equals("amogus");
        }
    }

    User user = new User(1L);
    MailInterface mailInterface = new DummyMailInterface();

    @Test
    public void testGetLettersCommandUnAuthed() {
        Command lettersListCommand = new LettersListCommand(mailInterface);
        Message message = lettersListCommand.execute(user, new String[]{"test@test.com", "3"});
        assertEquals(message.getText(), MessagesTemplates.NOT_AUTH_LIST_IS_UNAVAILABLE.text);
    }

    @Test
    public void testAuthCommandNotValid() {
        Command auth = new AuthCommand(mailInterface, Duration.ofSeconds(1));
        Message message = auth.execute(user, new String[]{"test@test.com", "123"});
        assertEquals(MessagesTemplates.AUTH_ERROR_MESSAGE.text, message.getText());
    }

    @Test
    public void testAuthCommandValid() {
        Command auth = new AuthCommand(mailInterface, Duration.ofSeconds(1));
        Message message = auth.execute(user, new String[]{"test@test.com", "amogus"});
        assertEquals(MessagesTemplates.AUTH_SUCCESS_MESSAGE.text, message.getText());
    }

    @Test
    public void testGetLettersCommandAuthed() {
        Command auth = new AuthCommand(mailInterface, Duration.ofSeconds(1));
        auth.execute(user, new String[]{"test@test.com", "amogus"});

        Command lettersListCommand = new LettersListCommand(mailInterface);
        Message message = lettersListCommand.execute(user, new String[]{"test@test.com", "3"});
        assertNotEquals(MessagesTemplates.NOT_AUTH_LIST_IS_UNAVAILABLE.text, message.getText());
    }

    @Test
    public void testSessionExpired() throws InterruptedException {
        Command auth = new AuthCommand(mailInterface, Duration.ofSeconds(1));
        auth.execute(user, new String[]{"test@test.com", "amogus"});
        Command lettersListCommand = new LettersListCommand(mailInterface);

        TimeUnit.SECONDS.sleep(1);

        Message message = lettersListCommand.execute(user, new String[]{"test@test.com", "3"});
        assertEquals(MessagesTemplates.SESSION_EXPIRED.text, message.getText());
    }
}
