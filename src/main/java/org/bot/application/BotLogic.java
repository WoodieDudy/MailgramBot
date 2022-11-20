package org.bot.application;

import org.bot.domain.Message;
import org.bot.domain.User;
import org.bot.domain.UserRepository;
import org.bot.domain.commands.Command;
import org.bot.enums.MessagesTemplates;

import java.util.Arrays;
import java.util.HashMap;


public class BotLogic {
    private final UserRepository userRepository = new UserRepository();
    private final HashMap<String, Command> commands = new HashMap<>();

    public BotLogic(Command[] commands) {
        for (Command command : commands) {
            this.commands.put(command.getAlias(), command);
        }
    }

    public Message getStartMessage() {
        return new Message(MessagesTemplates.START_MESSAGE.text);
    }

    public Message createResponse(Message message) {
        Long userId = message.getUserID();
        User user = userRepository.getUserById(userId);

        String[] args = message.getText().split(" ");

        String commandName = args[0];
        args = Arrays.copyOfRange(args, 1, args.length);

        if (commands.containsKey(commandName)) {
            return commands.get(commandName).execute(user, args);
        }
        return new Message(MessagesTemplates.DEFAULT_MESSAGE.text, userId);
    }
}
