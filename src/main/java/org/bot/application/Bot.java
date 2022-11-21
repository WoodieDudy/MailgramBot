package org.bot.application;

import org.bot.domain.Mailbox;
import org.bot.domain.Message;
import org.bot.domain.User;
import org.bot.domain.UserRepository;
import org.bot.domain.commands.AuthCommand;
import org.bot.domain.commands.Command;
import org.bot.domain.commands.HelpCommand;
import org.bot.domain.commands.LettersListCommand;
import org.bot.enums.MessagesTemplates;
import org.bot.infrastructure.interfaces.MailInterface;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;

import java.time.Duration;
import java.util.HashMap;

import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

public final class Bot extends AbilityBot {
    private final UserRepository userRepository = new UserRepository();
    private final HashMap<String, Command> commands;

    public Bot(MailInterface mailInterface, String BOT_TOKEN, String BOT_USERNAME) {
        super(BOT_TOKEN, BOT_USERNAME);

        this.commands = createCommands(mailInterface);
    }

    private static HashMap<String, Command> createCommands(MailInterface mailInterface) {
        HelpCommand helpCommand = new HelpCommand();
        Command[] commands = {
            new AuthCommand(mailInterface, Duration.ofDays(1)),
            new LettersListCommand(mailInterface),
            helpCommand
        };

        HashMap<String, Command> commandHashMap = new HashMap<>();
        for (Command command : commands) {
            commandHashMap.put(command.getAlias(), command);
        }

        helpCommand.generateHelpMessage(commands);
        return commandHashMap;
    }

    @Override
    public long creatorId() {
        return 123456789;
    }

    public Ability start() {
        return Ability
                .builder()
                .name("start")
                .info("Send start message")
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx -> silent.send(MessagesTemplates.START_MESSAGE.text, ctx.chatId()))
                .build();
    }

    public Ability help() {
        String abilityName = "help";
        return Ability
                .builder()
                .name(abilityName)
                .info("Returns help message")
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx -> {
                    User user = userRepository.getUserById(ctx.chatId());
                    silent.send(commands.get(abilityName).execute(user,  ctx.arguments()).getText(), ctx.chatId());
                })
                .build();
    }

    public Ability auth() {
        String abilityName = "auth";
        return Ability
                .builder()
                .name(abilityName)
                .info("Auth user")
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx -> {
                    User user = userRepository.getUserById(ctx.chatId());
                    silent.send(commands.get(abilityName).execute(user,  ctx.arguments()).getText(), ctx.chatId());
                })
                .build();
    }

    public Ability letters() {
        String abilityName = "letters";
        return Ability
                .builder()
                .name(abilityName)
                .info("Prints letters")
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx -> {
                    User user = userRepository.getUserById(ctx.chatId());
                    silent.send(commands.get(abilityName).execute(user,  ctx.arguments()).getText(), ctx.chatId());
                })
                .build();
    }
}