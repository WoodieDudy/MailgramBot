package org.bot.enums;

public enum MessagesTemplates {

    START_MESSAGE("Привет! Я Mailgram bot, я помогу тебе удобно пользоваться почтой без мобильного клиента " +
            "посредством Telegram. Чтобы узнать, что я умею, напиши /help"),
    HELP_MESSAGE("Я умею отправлять и принимать письма\n" +
            "Чтобы авторизоваться, напиши через пробел /auth\n" +
            "Чтобы отправить письмо, напиши /send\n" +
            "Чтобы узнать, какие письма у тебя есть, напиши /list"),

    DEFAULT_MESSAGE("Я не понимаю тебя. Чтобы узнать, что я умею, напиши /help"),
    DEFAULT_NOT_AUTH_MESSAGE("Я не понимаю тебя. Чтобы узнать, что я умею, напиши /help."),
    NOT_AUTH_LIST_IS_UNAVAILABLE("Невозможно получить список писем неавторизованного пользователя. " +
            "Воспользуйся командой /auth, чтобы залогиниться."),
    NOT_AUTH_SEND_IS_UNAVAILABLE("Нельзя отправлять письма без авторизации. Воспользуйся командой /auth, чтобы " +
            "войти."),

    WAITING_FOR_EMAIL("Введи почту"),
    WAITING_FOR_PASSWORD("Введите пароль"),
    AUTH_SUCCESS_MESSAGE("Авторизация успешна"),
    AUTH_UNAVAILABLE_MESSAGE("Вы уже авторизованы"),
    AUTH_INCORRECT_MESSAGE("Ошибка ввода, необходимо ввести /auth [адрес_почты] [пароль]"),
    AUTH_ERROR_MESSAGE("Ошибка! Проверь корректность введённого пароля и почты"),

    MAIL_ERROR_MESSAGE("Ошибка! Проверь корректность введённого адреса электронной почты"),

    SENDING_RECEIVER_MESSAGE("Введи электронный адрес получателя"),
    SENDING_THEME_MESSAGE("Введи тему сообщения, для отправки сообщения без темы введите /skip"),
    SENDING_TEXT_MESSAGE("Введи текст сообщения"),
    SENDING_SUCCESS_MESSAGE("Сообщение успешно отправлено"),
    SENDING_ERROR_MESSAGE("Сообщение не отправлено"),

    LIST_MESSAGE("Здесь будут отображаться последние n сообщений в твоём ящике"),

    FUNCTION_NOT_AVAILABLE("Функция находится в разработке"),

    ERROR_MESSAGE("Что-то пошло не так."),
    SESSION_EXPIRED("Сессия истекла, введите /auth для повторной авторизации");

    public final String text;

    MessagesTemplates(String text) {
        this.text = text;
    }
}