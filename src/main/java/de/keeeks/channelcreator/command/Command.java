package de.keeeks.channelcreator.command;

import de.keeeks.channelcreator.command.sender.CommandSender;
import lombok.Getter;

@Getter
public abstract class Command {

    public abstract void run(CommandSender sender, String[] args);

    public CommandDescription description() {
        return this.getClass().getDeclaredAnnotation(CommandDescription.class);
    }

    public String name() {
        return description().name();
    }

    public boolean isAlias(String name) {
        for (String alias : description().aliases()) {
            if (!alias.equalsIgnoreCase(name)) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass().getName().equals(obj.getClass().getName());
    }

    public String blankLine() {
        return "  ";
    }
}