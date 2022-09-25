package org.bot.domain;

public class MessagesTemplates {
    public static final String START_MESSAGE = "Привет! Я Mailgram bot, я помогу тебе удобно пользоваться почтой без мобильного клиента посредством Telegram. Чтобы узнать, что я умею, напиши /help.";
    public static final String HELP_MESSAGE = "Я умею отправлять и принимать письма.\n" +
            "Чтобы авторизоваться, напиши /auth.\n" +
            "Чтобы отправить письмо, напиши /send.\n" +
            "Чтобы прочитать письмо, напиши /read.\n" +
            "Чтобы узнать, какие письма у тебя есть, напиши /list.\n";
    public static final String DEFAULT_MESSAGE = "Я не понимаю тебя. Чтобы узнать, что я умею, напиши /help.";

    public static final String AUTH_MESSAGE = "Введи почту и пароль через пробел.";
    public static final String AUTH_PASSWORD_ERROR_MESSAGE = "Ошибка! Проверь корректность введённого пароля.";

    public static final String MAIL_ERROR_MESSAGE = "Ошибка! Проверь корректность введённого адреса электронной почты.";

    public static final String SENDING_RECEIVER_MESSAGE = "Введи электронный адрес получателя.";
    public static final String SENDING_THEME_MESSAGE = "Введи тему сообщения, для отправки сообщения без темы введите /skip.";
    public static final String SENDING_TEXT_MESSAGE = "Введи текст сообщения.";
    public static final String SENDING_SUCCESS_MESSAGE = "Сообщение успешно отправлено.";
    public static final String SENDING_ERROR_MESSAGE = "Сообщение не отправлено.";

}
