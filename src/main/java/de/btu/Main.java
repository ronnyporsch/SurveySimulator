package de.btu;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ChatGPTManager chatGPTManager = new ChatGPTManager(args[0]);
        System.out.println(chatGPTManager.ask("How are you?"));
    }
}