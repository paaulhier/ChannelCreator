package de.keeeks.channelcreator.logging;

import de.keeeks.channelcreator.util.Scheduler;
import org.jline.reader.LineReader;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class ConsoleLogger extends Logger {
    private final LogDispatcher dispatcher = new LogDispatcher(this);
    private final List<Consumer<LogRecord>> logConsumer = new LinkedList<>();

    private ConsoleLogger(LineReader reader) {
        super("[ChannelCreator]", null);
        setLevel(Level.ALL);

        try {
            FileHandler fileHandler = new FileHandler("latest.log", 1 << 24, 8, true);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new ConciseFormatter(false));
            addHandler(fileHandler);

            ColouredWriter consoleHandler = new ColouredWriter(reader);
            consoleHandler.setLevel(Level.ALL);
            consoleHandler.setFormatter(new ConciseFormatter(true));
            addHandler(consoleHandler);
        } catch (IOException ex) {
            System.err.println("Could not register logger!");
            ex.printStackTrace();
        }

        dispatcher.start();
    }

    public void addConsumer(Consumer<LogRecord> consumer) {
        logConsumer.add(consumer);
    }

    @Override
    public void log(LogRecord record) {
        dispatcher.queue(record);
    }

    void doLog(LogRecord record) {
        super.log(record);
        Scheduler.execute(() -> logConsumer.forEach(consumer -> consumer.accept(record)));
    }

    public static ConsoleLogger create(LineReader reader) {
        return new ConsoleLogger(reader);
    }
}