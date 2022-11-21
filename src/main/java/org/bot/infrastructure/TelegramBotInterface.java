//package org.bot.infrastructure;
//
//import org.bot.domain.Message;
//import org.bot.infrastructure.interfaces.BotInterface;
//
//public class TelegramBotInterface implements BotInterface {
//
//
//    public TelegramBotInterface(TelegramBot bot) {
//        this.bot = bot;
//    }
//
//    @Override
//    public Message readMessage() {
//        return null;
//    }
//
//    @Override
//    public void sendMessage(Message message) {
//        bot.execute(new SendMessage(message.getUserID(), message.getText()));
//    }
//}
