package org.bot.units;

import org.bot.domain.Mailbox;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MailboxTest {

    @Test
    public void testSessionNotExpired() {
        Mailbox mailbox = new Mailbox("johndoe@test.com", "password", Duration.ofSeconds(10));
        Boolean expectedResult = false;
        Boolean actualResult = mailbox.isSessionExpired();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testSessionExpired() throws InterruptedException {
        Mailbox mailbox = new Mailbox("johndoe@test.com", "password", Duration.ofSeconds(1));
        TimeUnit.SECONDS.sleep(1);
        Boolean expectedResult = true;
        Boolean actualResult = mailbox.isSessionExpired();
        assertEquals(expectedResult, actualResult);
    }
}
