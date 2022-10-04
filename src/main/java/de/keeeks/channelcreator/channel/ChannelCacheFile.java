package de.keeeks.channelcreator.channel;

import java.util.LinkedList;
import java.util.List;

public record ChannelCacheFile(
        List<Channel> channels
) {
    public static ChannelCacheFile create(List<Channel> channels) {
        return new ChannelCacheFile(channels);
    }

    public static ChannelCacheFile createDefault() {
        return new ChannelCacheFile(new LinkedList<>());
    }
}