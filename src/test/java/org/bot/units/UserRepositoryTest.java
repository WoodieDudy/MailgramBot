package org.bot.units;

import org.bot.domain.User;
import org.bot.domain.UserRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserRepositoryTest {
    @Test
    public void testAddGetUser() {
        UserRepository userRepository = new UserRepository();
        User user = new User(1L);
        userRepository.addUser(user);
        User expectedUser = user;
        User actualUser = userRepository.getUserById(1L);
        assertEquals(expectedUser, actualUser);
    }
}
