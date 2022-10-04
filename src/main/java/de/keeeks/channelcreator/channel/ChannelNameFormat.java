package de.keeeks.channelcreator.channel;

import java.util.List;

public record ChannelNameFormat(
        ChannelNamePrefixType prefixType,
        String format
) {
    private static final List<ChannelNameFormat> defaultChannelNameFormats = List.of(
            ChannelNameFormat.createWithDefaultFormat(ChannelNamePrefixType.HIGHEST),
            ChannelNameFormat.createWithDefaultFormat(ChannelNamePrefixType.DEFAULT),
            ChannelNameFormat.createWithDefaultFormat(ChannelNamePrefixType.LOWEST)
    );

    public static List<ChannelNameFormat> defaultChannelNameFormats() {
        return defaultChannelNameFormats;
    }

    public static ChannelNameFormat create(ChannelNamePrefixType prefixType, String format) {
        return new ChannelNameFormat(prefixType, format);
    }

    public static ChannelNameFormat createWithDefaultFormat(ChannelNamePrefixType prefixType) {
        return new ChannelNameFormat(prefixType, "╠ %channelGroup% » %romanNumber%");
    }
}