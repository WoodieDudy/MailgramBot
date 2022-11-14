package org.bot.application;

import org.bot.domain.*;
import org.bot.domain.commands.Command;
import org.bot.domain.commands.*;
import org.bot.enums.MessagesTemplates;
import org.bot.infrastructure.JakartaMailInterface;

import java.util.Arrays;
import java.util.HashMap;


public class BotLogic {
    private final UserRepository userRepository = new UserRepository();
    private final JakartaMailInterface mailInterface = new JakartaMailInterface();

    private final HashMap<String, Command> commands = new HashMap<String, Command>() {{
        put("/help", new HelpCommand(mailInterface));
        put("/letters", new LettersListCommand(mailInterface));
        put("/auth", new AuthCommand(mailInterface));
    }};

    public Message getStartMessage() { // Выдаёт стартовое сообщение.
        Message message = new Message(MessagesTemplates.START_MESSAGE.text);
        return message;
    }

    public Message createResponse(Message message) {
        Integer userID = message.getUserID();
        User user = userRepository.getUserById(userID);

        String[] args = message.getText().split(" ");
//        UserState userState = stateMachine.getUserState(userID);

        String commandName = args[0];
        args = Arrays.copyOfRange(args, 1, args.length);

        if (commands.containsKey(commandName)) {
            return commands.get(commandName).execute(user, args);
        }
        return new Message(MessagesTemplates.DEFAULT_MESSAGE.text);
    }
}
