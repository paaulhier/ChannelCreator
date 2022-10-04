package de.keeeks.channelcreator.exception;

public class QueryNotConnectedException extends RuntimeException {

    public QueryNotConnectedException() {
        super("The query seems not to be connected to the Server.");
    }
}