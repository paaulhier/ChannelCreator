package de.keeeks.channelcreator;

import de.keeeks.channelcreator.logging.ConsoleReader;

public class Bootstrap {
    public static void main(String[] args) {
        var consoleReader = ConsoleReader.newTerminalAndLineReader();
        var logger = consoleReader.getLogger();
        consoleReader.startReading();
        ChannelCreator.bootstrap(logger);
    }
}