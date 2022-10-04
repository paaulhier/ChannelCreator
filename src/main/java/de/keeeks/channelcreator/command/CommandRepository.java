package de.keeeks.channelcreator.command;

import de.keeeks.channelcreator.command.sender.CommandSender;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommandRepository {
    private static final Map<String, Command> commandMap = new ConcurrentHashMap<>();
    private final Logger logger;

    public void registerCommand(Command command) {
        if (commandClassRegistered(command)) {
            logger.log(Level.WARNING,
                    "Tried to register another instance of %s as command.".formatted(
                            command.getClass().getName()
                    )
            );
            return;
        }

        commandMap.put(command.name(), command);
        for (var alias : command.description().aliases()) {
            //an alias should never overwrite a name or over alias
            commandMap.putIfAbsent(alias, command);
        }
    }

    private boolean commandClassRegistered(Command command) {
        return commandMap.values().stream().anyMatch(target ->
                target.getClass().getName().equals(
                        command.getClass().getName()
                )
        );
    }

    public void registerCommands(Command... commands) {
        for (var command : commands) {
            registerCommand(command);
        }
    }

    public static void executeCommand(Logger logger,
                                      String name,
                                      String[] args,
                                      CommandSender sender) {
        var command = commandMap.get(name);

        if (command == null) {
            logger.log(
                    Level.WARNING,
                    "Command not found. Please provide a valid command."
            );
            return;
        }

        command.run(sender, args);
    }

    public static CommandRepository create(Logger logger) {
        return new CommandRepository(logger);
    }
}