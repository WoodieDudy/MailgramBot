package org.bot.domain;

import org.bot.exceptions.IdNotFoundException;

import java.util.HashMap;

public class UserRepository {
    private HashMap<Long, User> userRepository;

    public UserRepository() {
        userRepository = new HashMap<>();
    }

    public void addUser(User user) {
        Long id = user.getId();
        userRepository.put(id, user);
    }

    public void deleteUserById(Long id) throws IdNotFoundException {
        if (userRepository.containsKey(id)) {
            userRepository.remove(id);
            return;
        }
        throw new IdNotFoundException("There is no such identifier in the repository.");
    }

    public User getUserById(Long id) {
        if (!userRepository.containsKey(id)) {
            userRepository.put(id, new User(id));
        }
        return userRepository.get(id);
    }
}
