package org.bot.domain;

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

    public User getUserById(Long id) {
        if (!userRepository.containsKey(id)) {
            userRepository.put(id, new User(id));
        }
        return userRepository.get(id);
    }
}
