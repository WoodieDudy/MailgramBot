package org.bot.units;

import org.bot.domain.Mailbox;
import org.bot.domain.User;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {
    User user = new User(1L);

    @Test
    public void testGetId() {
        Long expectedId = 1L;
        Long actualId = user.getId();
        assertEquals(expectedId, actualId);
    }

    @Test
    public void testAddNewMailbox() {
        user.addNewMailbox(new Mailbox("johndoe@test.com", "password", Duration.ofHours(10)));
        List<String> expectedMailboxes = new ArrayList<>();
        expectedMailboxes.add("johndoe@test.com");
        List<String> actualMailbox = user.getAllEmails();
        assertEquals(expectedMailboxes, actualMailbox);
    }

    @Test
    public void testGetMailbox() {
        Mailbox expectedMailbox = new Mailbox("johndoe@test.com", "password", Duration.ofHours(10));
        user.addNewMailbox(new Mailbox("johndoe@test.com", "password", Duration.ofHours(10)));
        Mailbox actualMailbox = user.getMailbox("johndoe@test.com");
        assertEquals(expectedMailbox, actualMailbox);
    }

    @Test
    public void testGetAllEmails() {
        user.addNewMailbox(new Mailbox("johndoe@test.com", "password", Duration.ofHours(10)));
        user.addNewMailbox(new Mailbox("janedoe@test.com", "password", Duration.ofHours(10)));
        List<String> expectedMailboxes = new ArrayList<>();
        expectedMailboxes.add("janedoe@test.com");
        expectedMailboxes.add("johndoe@test.com");
        List<String> actualMailboxes = new ArrayList<>(user.getAllEmails());
        Collections.sort(expectedMailboxes);
        Collections.sort(actualMailboxes);
        assertEquals(expectedMailboxes, actualMailboxes);
    }
}
