package org.bot.application;

import org.bot.domain.Mailbox;
import org.bot.domain.User;
import org.bot.domain.UserRepository;
import org.bot.domain.commands.AuthCommand;
import org.bot.domain.commands.Command;
import org.bot.domain.commands.HelpCommand;
import org.bot.domain.commands.LettersListCommand;
import org.bot.enums.MessagesTemplates;
import org.bot.infrastructure.interfaces.MailInterface;
import org.checkerframework.checker.units.qual.A;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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

    @Override
    public void onUpdateReceived(Update update) {
        super.onUpdateReceived(update);
        if (!update.hasCallbackQuery()) {
            return;
        }
        System.out.println(update.getCallbackQuery().getData());

        String callbackData = update.getCallbackQuery().getData();
        String[] callbackDataParts = callbackData.split(" ");
        String commandAlias = callbackDataParts[0];
        String fromMessageId = callbackDataParts[1];
        if (commandAlias.equals("letters")) {
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
            List<InlineKeyboardButton> Buttons = new ArrayList<InlineKeyboardButton>();
            InlineKeyboardButton github= new InlineKeyboardButton("thx!");
            github.setCallbackData("letters 123");
            Buttons.add(github);
            keyboard.add(Buttons);
            inlineKeyboardMarkup.setKeyboard(keyboard);

            EditMessageReplyMarkup newKeyboard = new EditMessageReplyMarkup();
            newKeyboard.setChatId(update.getCallbackQuery().getMessage().getChatId());
            newKeyboard.setMessageId(Integer.parseInt(fromMessageId));
            newKeyboard.setReplyMarkup(inlineKeyboardMarkup);
            silent.execute(newKeyboard);
        }
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
                    SendChatAction sendChatAction = new SendChatAction(); // TODO move to BotInterface
                    sendChatAction.setChatId(ctx.chatId());
                    sendChatAction.setAction(ActionType.TYPING);
                    silent.execute(sendChatAction);
                    silent.send(commands.get(abilityName).execute(user,  ctx.arguments()).getText(), ctx.chatId());
                })
                .build();
    }

    public Ability test() {
        String abilityName = "test";
        return Ability
                .builder()
                .name(abilityName)
                .info("Prints letters")
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx -> {
                    SendMessage message = new SendMessage();
                    message.setChatId(ctx.chatId());
                    message.setText("Inline model below.");

                    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
                    List<InlineKeyboardButton> Buttons = new ArrayList<InlineKeyboardButton>();

                    InlineKeyboardButton github = new InlineKeyboardButton("push me");
                    github.setCallbackData("letters" + " " + (ctx.update().getMessage().getMessageId() + 1));
                    Buttons.add(github);

                    keyboard.add(Buttons);
                    inlineKeyboardMarkup.setKeyboard(keyboard);
                    message.setReplyMarkup(inlineKeyboardMarkup);

                    Optional<Message> sys = silent.execute(message);
                    sys.ifPresent(value -> System.out.println("sys" + value.getMessageId()));
                })
                .build();
    }
}