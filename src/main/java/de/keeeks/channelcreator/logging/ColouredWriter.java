package de.keeeks.channelcreator.logging;

import org.fusesource.jansi.Ansi;
import org.jline.reader.LineReader;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.regex.Pattern;

public class ColouredWriter extends Handler {

    private record ReplacementSpecification(Pattern pattern, String replacement) {
    }

    private static ReplacementSpecification compile(ChatColor color, String ansi) {
        return new ReplacementSpecification(Pattern.compile("(?i)" + color.toString()), ansi);
    }

    private static final ReplacementSpecification[] REPLACEMENTS = new ReplacementSpecification[]
            {
                    compile(ChatColor.BLACK, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).boldOff().toString()),
                    compile(ChatColor.DARK_BLUE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).boldOff().toString()),
                    compile(ChatColor.DARK_GREEN, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).boldOff().toString()),
                    compile(ChatColor.DARK_AQUA, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).boldOff().toString()),
                    compile(ChatColor.DARK_RED, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).boldOff().toString()),
                    compile(ChatColor.DARK_PURPLE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).boldOff().toString()),
                    compile(ChatColor.GOLD, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).boldOff().toString()),
                    compile(ChatColor.GRAY, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).boldOff().toString()),
                    compile(ChatColor.DARK_GRAY, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).bold().toString()),
                    compile(ChatColor.BLUE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).bold().toString()),
                    compile(ChatColor.GREEN, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).bold().toString()),
                    compile(ChatColor.AQUA, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).bold().toString()),
                    compile(ChatColor.RED, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).bold().toString()),
                    compile(ChatColor.LIGHT_PURPLE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).bold().toString()),
                    compile(ChatColor.YELLOW, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).bold().toString()),
                    compile(ChatColor.WHITE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).bold().toString()),
                    compile(ChatColor.MAGIC, Ansi.ansi().a(Ansi.Attribute.BLINK_SLOW).toString()),
                    compile(ChatColor.BOLD, Ansi.ansi().a(Ansi.Attribute.UNDERLINE_DOUBLE).toString()),
                    compile(ChatColor.STRIKETHROUGH, Ansi.ansi().a(Ansi.Attribute.STRIKETHROUGH_ON).toString()),
                    compile(ChatColor.UNDERLINE, Ansi.ansi().a(Ansi.Attribute.UNDERLINE).toString()),
                    compile(ChatColor.ITALIC, Ansi.ansi().a(Ansi.Attribute.ITALIC).toString()),
                    compile(ChatColor.RESET, Ansi.ansi().a(Ansi.Attribute.RESET).toString()),
            };
    //
    private final LineReader lineReader;

    public ColouredWriter(LineReader lineReader) {
        this.lineReader = lineReader;
    }

    public void print(String s) {
        for (ReplacementSpecification replacement : REPLACEMENTS) {
            s = replacement.pattern.matcher(s).replaceAll(replacement.replacement);
        }
        lineReader.printAbove(
                Ansi.ansi().eraseLine(Ansi.Erase.ALL).toString() + "\r" + s + Ansi.ansi().reset().toString()
        );
    }

    @Override
    public void publish(LogRecord record) {
        if (isLoggable(record)) {
            print(getFormatter().format(record));
        }
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }
}