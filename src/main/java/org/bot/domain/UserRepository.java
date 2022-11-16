package org.bot.domain;

import org.bot.exceptions.IdNotFoundException;

import java.util.HashMap;

public class UserRepository {
    private HashMap<Integer, User> userRepository;

    public UserRepository() {
        userRepository = new HashMap<>();
    }

    public void addUser(User user) {
        Integer id = user.getID();
        userRepository.put(id, user);
    }

    public void deleteUserById(Integer id) throws IdNotFoundException {
        if (userRepository.containsKey(id)) {
            userRepository.remove(id);
            return;
        }
        throw new IdNotFoundException("There is no such identifier in the repository.");
    }

    public User getUserById(Integer id) {
        if (!userRepository.containsKey(id)) {
            userRepository.put(id, new User(id));
        }
        return userRepository.get(id);
    }
}
