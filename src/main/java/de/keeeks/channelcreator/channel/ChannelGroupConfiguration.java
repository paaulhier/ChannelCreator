package de.keeeks.channelcreator.channel;

import java.util.List;

public record ChannelGroupConfiguration(
        List<ChannelGroup> channelGroups
) {

    public static ChannelGroupConfiguration create(List<ChannelGroup> channelGroups) {
        return new ChannelGroupConfiguration(channelGroups);
    }

    public static ChannelGroupConfiguration createWithDefaults() {
        return create(List.of(
                ChannelGroup.create("lounge",
                        "Lounge",
                        ChannelGroupSettings.createDefault(),
                        ChannelNameFormat.defaultChannelNameFormats()),
                ChannelGroup.create("talk",
                        "Talk",
                        ChannelGroupSettings.createDefault(),
                        ChannelNameFormat.defaultChannelNameFormats())
        ));
    }
}