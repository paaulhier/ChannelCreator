package de.keeeks.channelcreator.channel;

public record ChannelGroupSettings(int minChannels,
                                   int maxChannels,
                                   long parentId,
                                   boolean deleteIfEmpty,
                                   boolean subChannel) {

    public static ChannelGroupSettings create(int minChannels,
                                              int maxChannels,
                                              long parentId,
                                              boolean deleteIfEmpty,
                                              boolean subChannel) {
        return new ChannelGroupSettings(minChannels,
                maxChannels,
                parentId,
                deleteIfEmpty,
                subChannel);
    }

    public static ChannelGroupSettings createDefault() {
        return new ChannelGroupSettings(1, 1, 0, true, false);
    }
}