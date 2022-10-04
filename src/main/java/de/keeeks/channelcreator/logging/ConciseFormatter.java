package de.keeeks.channelcreator.logging;

import lombok.RequiredArgsConstructor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Clock;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

@RequiredArgsConstructor
public class ConciseFormatter extends Formatter {

    private static final DateTimeFormatter formatter = DateTimeFormatter
            .ofLocalizedDateTime(FormatStyle.SHORT)
            .withZone(ZoneId.systemDefault());
    private final Clock clock = Clock.systemDefaultZone();
    private final boolean coloured;

    @Override
    public String format(LogRecord record) {
        var formatted = "[%s] %s: %s".formatted(
                formatter.format(clock.instant()),
                formatLevel(record.getLevel()),
                formatMessage(record)
        );

        if (record.getThrown() != null) {
            StringWriter writer = new StringWriter();
            record.getThrown().printStackTrace(new PrintWriter(writer));
            formatted += writer;
        }

        return formatted.concat("\n");
    }

    private String formatLevel(Level level) {
        if (!coloured) {
            return level.getLocalizedName();
        }

        ChatColor color;

        if (level == Level.INFO) {
            color = ChatColor.BLUE;
        } else if (level == Level.WARNING) {
            color = ChatColor.YELLOW;
        } else if (level == Level.SEVERE) {
            color = ChatColor.RED;
        } else {
            color = ChatColor.AQUA;
        }

        return color + level.getLocalizedName() + ChatColor.RESET;
    }
}