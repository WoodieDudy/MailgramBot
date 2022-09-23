package org.example;


import java.util.Scanner;


public class ConsoleBotInterface extends BotInterface {

    private Scanner in = new Scanner(System.in);
    @Override
    public Message readMessage() {
        String text = in.nextLine();
        return new Message(text);
    }
    @Override
    public void sendMessage(Message message) {
        System.out.println(message.text);
    }
}
