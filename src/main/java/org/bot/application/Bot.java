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
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodSerializable;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
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
        // print webapp info
        System.out.println(update.getMessage().getWebAppData().getData());

        if (!update.hasCallbackQuery()) {
            return;
        }
        User user = userRepository.getUserById(update.getCallbackQuery().getFrom().getId());
        System.out.println(update.getCallbackQuery().getData());

        String callbackData = update.getCallbackQuery().getData();
        String[] callbackDataParts = callbackData.split(" ");
        String commandAlias = callbackDataParts[0];
        String fromMessageId = callbackDataParts[1];
        if (commandAlias.equals("letters")) {
            user.setTempEmail(callbackDataParts[2]);

            List<InlineKeyboardButton> buttons = new ArrayList<>();
            for (int lettersNumber: new int[]{1, 2, 4}) {
                InlineKeyboardButton button = new InlineKeyboardButton(String.valueOf(lettersNumber));
                button.setCallbackData("chooseNum " + fromMessageId + " " + lettersNumber);
                buttons.add(button);
            }

            System.out.println(fromMessageId);

            org.bot.domain.Message messageToEdit = new org.bot.domain.Message(
                "Выберите количество писем",
               Integer.parseInt(fromMessageId),
               update.getCallbackQuery().getFrom().getId(),
               buttons
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
                    System.out.println(ctx.chatId());
                    User user = userRepository.getUserById(ctx.chatId());
                    SendChatAction sendChatAction = new SendChatAction(); // TODO move to BotInterface
                    sendChatAction.setChatId(ctx.chatId());
                    sendChatAction.setAction(ActionType.TYPING);
                    List<String> emails = user.getAllEmails();
                    if (emails.isEmpty()) {
                        silent.send(MessagesTemplates.NOT_AUTH_LIST_IS_UNAVAILABLE.text, ctx.chatId());
                        return;
                    }
                    org.bot.domain.Message message = new org.bot.domain.Message(
                        MessagesTemplates.CHOOSE_EMAIL.text,
                        ctx.chatId()
                    );

                    SendMessage messageToSend = TelegramBotInterface.sendMessage(message);

                    Optional<Message> sentMessage = silent.execute(messageToSend);

                    if (sentMessage.isEmpty()) {
                        System.out.println("Message is empty");
                        return;
                    }
                    Integer sentMessageId = sentMessage.get().getMessageId();

                    List<InlineKeyboardButton> buttons = new ArrayList<>();
                    for (String email : emails) {
                        InlineKeyboardButton button = new InlineKeyboardButton(email);
                        button.setCallbackData("letters " + sentMessageId + " " + email);
                        buttons.add(button);
                    }

                    org.bot.domain.Message messageToEdit = new org.bot.domain.Message(
                        sentMessageId,
                        ctx.chatId(),
                        buttons
                    );

                    List<BotApiMethodSerializable> executable = TelegramBotInterface.editMessage(messageToEdit);

                    for (BotApiMethodSerializable botApiMethodSerializable : executable) {
                        silent.execute(botApiMethodSerializable);
                    }

//                    silent.send(commands.get(abilityName).execute(user,  ctx.arguments()).getText(), ctx.chatId());
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
                    System.out.println(ctx.chatId());
                    User user = userRepository.getUserById(ctx.chatId());

                    WebAppInfo webAppInfo = new WebAppInfo();
                    webAppInfo.setUrl("https://e14f-40-87-132-117.eu.ngrok.io");

                    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                    replyKeyboardMarkup.setSelective(true);
                    replyKeyboardMarkup.setResizeKeyboard(true);
                    replyKeyboardMarkup.setOneTimeKeyboard(false);

                    List<KeyboardRow> keyboard = new ArrayList<>();

                    KeyboardRow keyboardFirstRow = new KeyboardRow();

                    KeyboardButton btn = new KeyboardButton("test");
                    btn.setWebApp(webAppInfo);
                    keyboardFirstRow.add(btn);

                    keyboard.add(keyboardFirstRow);
                    replyKeyboardMarkup.setKeyboard(keyboard);

                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(ctx.chatId());
                    sendMessage.setText("test");
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