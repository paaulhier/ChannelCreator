package de.keeeks.channelcreator.configuration;

public record TeamSpeakConfiguration(String hostname,
                                     int queryPort,
                                     int port,
                                     String username,
                                     String password,
                                     String nickname) {

    public static TeamSpeakConfiguration createDefault() {
        return create("127.0.0.1",
                10011,
                9987,
                "serveradmin",
                "password",
                "ChannelCreator");
    }

    public static TeamSpeakConfiguration create(String hostname,
                                                int queryPort,
                                                int port,
                                                String username,
                                                String password,
                                                String nickname) {
        return new TeamSpeakConfiguration(hostname, queryPort, port, username, password, nickname);
    }
}