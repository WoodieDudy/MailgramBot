package org.bot.enums;

public enum MessagesTemplates {

    START_MESSAGE("Привет! Я Mailgram bot, я помогу тебе удобно пользоваться почтой без " +
            "мобильного клиента посредством Telegram. Чтобы узнать, что я умею, напиши `/help`"),
    DEFAULT_MESSAGE("Я не понимаю тебя. Чтобы узнать, что я умею, напиши `/help`"),
    NOT_AUTH_LIST_IS_UNAVAILABLE("Невозможно получить список писем неавторизованного пользователя. " +
            "Воспользуйся командой `/auth`, чтобы залогиниться"),
    NOT_AUTH_SEND_IS_UNAVAILABLE("Нельзя отправлять письма без авторизации. Воспользуйся командой `/auth`, чтобы " +
            "войти"),

    AUTH_COMMAND_MESSAGE("Воспользуйтесь кнопкой под строкой ввода для авторизации"),
    AUTH_SUCCESS_MESSAGE("Авторизация успешна"),
    AUTH_ERROR_MESSAGE("Ошибка! Проверь корректность введённого пароля и почты"),



    ERROR_MESSAGE("Что-то пошло не так."),
    SESSION_EXPIRED("Сессия истекла, введите `/auth` для повторной авторизации"),
    INCORRECT_ARGS("Некорректные параметры команды, введите `/help` для получения справки"),

    CHOOSE_EMAIL("Выберите почту"),
    CHOOSE_NUMBER("Выберите количество писем");
    public final String text;

    MessagesTemplates(String text) {
        this.text = text;
    }
}