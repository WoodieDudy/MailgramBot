package org.bot.enums;

public enum MessagesTemplates {

    START_MESSAGE("Привет! Я Mailgram bot, я помогу тебе удобно пользоваться почтой без мобильного клиента " +
            "посредством Telegram. Чтобы узнать, что я умею, напиши /help"),
    HELP_MESSAGE("Я умею отправлять и принимать письма\n" +
            "Чтобы авторизоваться, напиши через пробел /auth [адрес_почты] [пароль]\n" +
            "Чтобы отправить письмо, напиши /send\n" +
            "Чтобы узнать, какие письма у тебя есть, напиши /list"),
    DEFAULT_MESSAGE("Я не понимаю тебя. Чтобы узнать, что я умею, напиши /help"),
    DEFAULT_NOT_AUTH_MESSAGE("Я не понимаю тебя. Чтобы узнать, что я умею, напиши /help. Обрати внимание: " +
            "неавторизованные пользователи могут использовать только команды /help и /auth"),

    AUTH_MESSAGE("Введи почту и пароль через пробел"),
    AUTH_SUCCESS_MESSAGE("Авторизация успешна"),
    AUTH_UNAVAILABLE_MESSAGE("Вы уже авторизованы"),
    AUTH_INCORRECT_MESSAGE("Ошибка ввода, необходимо ввести /auth [адрес_почты] [пароль]"),
    AUTH_PASSWORD_ERROR_MESSAGE("Ошибка! Проверь корректность введённого пароля"),

    MAIL_ERROR_MESSAGE("Ошибка! Проверь корректность введённого адреса электронной почты"),

    SENDING_RECEIVER_MESSAGE("Введи электронный адрес получателя"),
    SENDING_THEME_MESSAGE("Введи тему сообщения, для отправки сообщения без темы введите /skip"),
    SENDING_TEXT_MESSAGE("Введи текст сообщения"),
    SENDING_SUCCESS_MESSAGE("Сообщение успешно отправлено"),
    SENDING_ERROR_MESSAGE("Сообщение не отправлено"),

    LIST_MESSAGE("Здесь будут отображаться последние n сообщений в твоём ящике"),

    FUNCTION_NOT_AVAILABLE("Функция находится в разработке");
    public final String text;

    MessagesTemplates(String text) {
        this.text = text;
    }
}