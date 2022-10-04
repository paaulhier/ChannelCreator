package de.keeeks.channelcreator.command.sender;

public interface CommandSender {
    void sendMessage(String message);

    void sendMessage(String... message);

    String name();
}