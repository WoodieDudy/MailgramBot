package org.bot.application;

import org.bot.domain.User;
import org.bot.domain.UserRepository;
import org.bot.application.commands.AuthCommand;
import org.bot.application.commands.Command;
import org.bot.application.commands.HelpCommand;
import org.bot.application.commands.LettersListCommand;
import org.bot.enums.MessagesTemplates;
import org.bot.infrastructure.TelegramBotInterface;
import org.bot.infrastructure.interfaces.MailInterface;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodSerializable;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;

import java.time.Duration;
import java.util.*;

import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

public final class Bot extends AbilityBot {
    private final UserRepository userRepository = new UserRepository();
    private final HashMap<String, Command> commands;
    private final String WEBAPP_URL;

    public Bot(MailInterface mailInterface, String BOT_TOKEN, String BOT_USERNAME, String WEBAPP_URL) {
        super(BOT_TOKEN, BOT_USERNAME);
        this.WEBAPP_URL = WEBAPP_URL;
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

        if (update.hasCallbackQuery()) {
            User user = userRepository.getUserById(update.getCallbackQuery().getFrom().getId());
            String callbackData = update.getCallbackQuery().getData();
            String[] callbackDataParts = callbackData.split(" ");
            String commandAlias = callbackDataParts[0];
            String messageId = callbackDataParts[1];

            if (commandAlias.equals("letters")) {
                user.setTempEmail(callbackDataParts[2]);

                org.bot.domain.Message messageToEdit = new org.bot.domain.Message(
                        MessagesTemplates.CHOOSE_NUMBER.text,
                        Integer.parseInt(messageId),
                        update.getCallbackQuery().getFrom().getId(),
                        TelegramBotInterface.numberOfLetters(messageId)
                );

                List<BotApiMethodSerializable> executable = TelegramBotInterface.editMessage(messageToEdit);

                for (BotApiMethodSerializable botApiMethodSerializable : executable) {
                    silent.execute(botApiMethodSerializable);
                }
            }
            else if (commandAlias.equals("chooseNum")) {
                List<String> args = Arrays.asList(user.getTempEmail(), callbackDataParts[2]);
                sendAll(commands.get("letters").execute(user, args), update.getCallbackQuery().getFrom().getId());
            }
        }

        if (update.getMessage().getWebAppData() != null) {
            User user = userRepository.getUserById(update.getMessage().getChatId());

            String callbackData = update.getMessage().getWebAppData().getData();
            String[] callbackDataParts = callbackData.split(" ");
            String email = callbackDataParts[0];
            String password = callbackDataParts[1];
            sendAll(commands.get("auth").execute(user, List.of(email, password)), user.getId());
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
                    sendAll(commands.get(abilityName).execute(user, List.of(ctx.arguments())), ctx.chatId());
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
                    List<String> emails = user.getAllEmails();

                    Optional<Message> sentMessage = silent.execute(
                            TelegramBotInterface.lettersAliasEmailWithoutKeyboard(user, emails));

                    if (sentMessage.isEmpty()) {
                        System.out.println("Message is empty");
                        return;
                    }

                    List<BotApiMethodSerializable> executable = TelegramBotInterface.lettersAliasEmailWithKeyboard(
                            user,
                            emails,
                            sentMessage
                    );

                    for (BotApiMethodSerializable botApiMethodSerializable : executable) {
                        silent.execute(botApiMethodSerializable);
                    }
                })
                .build();
    }

    public Ability auth() {
        String abilityName = "auth";
        return Ability
                .builder()
                .name(abilityName)
                .info("Authenticate user")
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx -> {
                    System.out.println("Auth " + ctx.chatId());

                    WebAppInfo webAppInfo = new WebAppInfo();
                    webAppInfo.setUrl(this.WEBAPP_URL);

                    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                    replyKeyboardMarkup.setSelective(true);
                    replyKeyboardMarkup.setResizeKeyboard(true);
                    replyKeyboardMarkup.setOneTimeKeyboard(true);

                    List<KeyboardRow> keyboard = new ArrayList<>();

                    KeyboardRow keyboardFirstRow = new KeyboardRow();

                    KeyboardButton btn = new KeyboardButton("????????????????????????????");
                    btn.setWebApp(webAppInfo);
                    keyboardFirstRow.add(btn);

                    keyboard.add(keyboardFirstRow);
                    replyKeyboardMarkup.setKeyboard(keyboard);
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(ctx.chatId());
                    sendMessage.setText(MessagesTemplates.AUTH_COMMAND_MESSAGE.text);
                    sendMessage.setReplyMarkup(replyKeyboardMarkup);
                    silent.execute(sendMessage);
                })
                .build();
    }

    private void sendAll(List<org.bot.domain.Message> messages, Long userId) {
        List<SendMessage> sendMessageList = TelegramBotInterface.sendMessageList(messages);
        for (SendMessage sendMessage : sendMessageList) {
            sendMessage.setChatId(userId);
            silent.execute(sendMessage);
        }
    }
}