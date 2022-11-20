package org.bot.infrastructure;


import org.bot.infrastructure.interfaces.BotInterface;
import org.bot.domain.Message;

import java.util.Scanner;

public class ConsoleBotInterface implements BotInterface {

    private Scanner in = new Scanner(System.in);

    public Message readMessage() {
        String text = in.nextLine();
        return new Message(text, 0L);
    }

    public void sendMessage(Message message) {
        System.out.println(message.getText());
    }
}
