package de.keeeks.channelcreator.command.sender;

import lombok.RequiredArgsConstructor;

import java.util.logging.Level;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class ConsoleCommandSender implements CommandSender {
    private final Logger logger;

    @Override
    public void sendMessage(String message) {
        logger.log(Level.INFO, message);
    }

    @Override
    public void sendMessage(String... message) {
        for (var s : message) {
            sendMessage(s);
        }
    }

    @Override
    public String name() {
        return "Console";
    }
}