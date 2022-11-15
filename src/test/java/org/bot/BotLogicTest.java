package org.bot;

import org.bot.domain.Letter;
import org.bot.domain.Mailbox;
import org.bot.domain.Message;
import org.bot.domain.User;
import org.bot.domain.commands.AuthCommand;
import org.bot.domain.commands.Command;
import org.bot.domain.commands.LettersListCommand;
import org.bot.enums.MessagesTemplates;
import org.bot.infrastructure.interfaces.MailInterface;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BotLogicTest {
    static class DummyMailInterface implements MailInterface {
        @Override
        public Letter[] readMessages(Mailbox mailbox, int lettersCount) {
            return new Letter[0];
        }

        @Override
        public void sendMessage(Mailbox mailbox, Letter letter) {}

        @Override
        public boolean isCredentialsCorrect(Mailbox mailbox) {
            return mailbox.getEmail().equals("test@test.com") && mailbox.getPassword().equals("amogus");
        }
    }

    User user = new User(1);
    MailInterface mailInterface = new DummyMailInterface();

    @Test
    public void testGetLettersCommandUnAuthed() {
        Command command = new LettersListCommand(mailInterface);
        Message message = command.execute(user, new String[]{"test@test.com", "3"});
        assertEquals(message.getText(), MessagesTemplates.NOT_AUTH_LIST_IS_UNAVAILABLE.text);
    }

    @Test
    public void testAuthCommandNotValid() {
        Command auth = new AuthCommand(mailInterface);
        Message message = auth.execute(user, new String[]{"test@test.com", "123"});
        assertEquals(MessagesTemplates.AUTH_ERROR_MESSAGE.text, message.getText());
    }

    @Test
    public void testAuthCommandValid() {
        Command auth = new AuthCommand(mailInterface);
        Message message = auth.execute(user, new String[]{"test@test.com", "amogus"});
        assertEquals(user.getMailbox("test@test.com").getEmail(), "test@test.com");
        assertEquals(MessagesTemplates.AUTH_SUCCESS_MESSAGE.text, message.getText());
    }

    @Test
    public void testGetLettersCommandAuthed() {
        Command auth = new AuthCommand(mailInterface);
        auth.execute(user, new String[]{"test@test.com", "amogus"});

        Command command = new LettersListCommand(mailInterface);
        Message message = command.execute(user, new String[]{"test@test.com", "3"});
        assertNotEquals(message.getText(), MessagesTemplates.NOT_AUTH_LIST_IS_UNAVAILABLE.text);
    }
}
