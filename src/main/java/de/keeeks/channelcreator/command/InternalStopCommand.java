package de.keeeks.channelcreator.command;

import de.keeeks.channelcreator.command.sender.CommandSender;

@CommandDescription(name = "stop", aliases = {"end", "exit", "quit"})
public class InternalStopCommand extends Command{
    @Override
    public void run(CommandSender sender, String[] args) {
        System.exit(0);
    }

    public static InternalStopCommand create() {
        return new InternalStopCommand();
    }
}