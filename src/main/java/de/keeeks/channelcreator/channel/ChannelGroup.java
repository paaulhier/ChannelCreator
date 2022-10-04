package de.keeeks.channelcreator.channel;

import java.util.List;

public record ChannelGroup(String groupName,
                           String groupDisplayName,
                           ChannelGroupSettings channelGroupSettings,
                           List<ChannelNameFormat> channelNameFormats) {

    public boolean enoughChannels(int amount) {
        return amount > channelGroupSettings.minChannels();
    }

    public static ChannelGroup create(String groupName,
                                      String groupDisplayName,
                                      ChannelGroupSettings channelGroupSettings,
                                      List<ChannelNameFormat> channelNameFormats) {
        return new ChannelGroup(groupName,
                groupDisplayName,
                channelGroupSettings,
                channelNameFormats);
    }
}