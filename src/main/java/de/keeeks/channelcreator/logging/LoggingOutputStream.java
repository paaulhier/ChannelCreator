package de.keeeks.channelcreator.logging;

import lombok.RequiredArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class LoggingOutputStream extends ByteArrayOutputStream {

    private static final String separator = System.getProperty("line.separator");
    /*========================================================================*/
    private final Logger logger;
    private final Level level;

    @Override
    public void flush() {
        String contents = toString(StandardCharsets.UTF_8);
        super.reset();
        if (!contents.isEmpty() && !contents.equals(separator)) {
            logger.logp(level, "", "", contents);
        }
    }
}