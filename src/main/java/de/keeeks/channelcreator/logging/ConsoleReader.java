package de.keeeks.channelcreator.logging;

import de.keeeks.channelcreator.command.CommandRepository;
import de.keeeks.channelcreator.command.sender.CommandSender;
import de.keeeks.channelcreator.command.sender.ConsoleCommandSender;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.fusesource.jansi.AnsiConsole;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ConsoleReader {
    private static final String[] emptyArray = new String[]{};

    private final ConsoleLogger logger;
    private final Terminal terminal;
    private final LineReader lineReader;
    private final CommandSender consoleSender;

    public void startReading() {
        var thread = new Thread(() -> {
            String line;
            try {
                while ((line = lineReader.readLine("> ")) != null) {
                    var arguments = line.split(" ");
                    if (arguments.length == 1) {
                        CommandRepository.executeCommand(
                                logger,
                                arguments[0],
                                emptyArray,
                                consoleSender
                        );
                    } else {
                        CommandRepository.executeCommand(
                                logger,
                                arguments[0],
                                commandArguments(arguments),
                                consoleSender
                        );
                    }
                }
            } catch (UserInterruptException e) {
                System.exit(0);
            }
        });
        thread.start();
    }

    public void clear() {
        lineReader.callWidget(LineReader.CLEAR_SCREEN);
    }

    private String[] commandArguments(String[] arguments) {
        return Arrays.copyOfRange(
                arguments,
                0,
                arguments.length
        );
    }


    @SneakyThrows
    public static ConsoleReader newTerminalAndLineReader() {
        var terminal = buildTerminal();
        var lineReader = buildLineReader(terminal);
        var logger = ConsoleLogger.create(lineReader);
        AnsiConsole.systemInstall();
        return new ConsoleReader(
                logger,
                terminal,
                lineReader,
                new ConsoleCommandSender(logger)
        );
    }

    private static LineReader buildLineReader(Terminal terminal) {
        return LineReaderBuilder.builder()
                .terminal(terminal)
                .option(
                        LineReader.Option.INSERT_BRACKET,
                        true
                ).build();
    }

    private static Terminal buildTerminal() throws IOException {
        return TerminalBuilder.builder()
                .system(true)
                .jansi(true)
                .encoding(StandardCharsets.UTF_8)
                .build();
    }
}