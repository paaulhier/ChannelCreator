package de.keeeks.channelcreator.channel;

import de.keeeks.channelcreator.ChannelCreator;
import de.keeeks.channelcreator.exception.QueryNotConnectedException;
import de.keeeks.channelcreator.file.JsonFile;
import de.keeeks.channelcreator.util.Scheduler;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Getter
@Accessors(fluent = true)
public final class ChannelRepository {
    private static final JsonFile channelsCacheFile = new JsonFile("cache", "channels");
    private static final JsonFile channelGroupsFile = new JsonFile("config", "groups");

    private final List<ChannelGroup> channelGroups = new LinkedList<>();
    private final List<Channel> channelList = new LinkedList<>();

    private final ChannelCreator channelCreator;

    private ChannelRepository(Logger logger, ChannelCreator channelCreator) {
        this.channelCreator = channelCreator;
        loadChannelGroupsFromConfig();
        loadChannelsFromCacheFile();
        startChannelAmountChecker(logger);

        Runtime.getRuntime().addShutdownHook(new Thread(this::saveChannelsToFileCache));
    }

    public List<Channel> channelsOfGroup(ChannelGroup channelGroup) {
        return channelList.stream().filter(
                channel -> channel.channelGroup().groupName().equals(channelGroup.groupName())
        ).collect(Collectors.toList());
    }

    // TODO: 04.10.2022 Implement teamspeak channel user count query
    public List<Channel> emptyChannels(List<Channel> channels) throws QueryNotConnectedException {
        var ts3Api = channelCreator.ts3Api();
        return channels.stream().filter(channel -> {
            var channelInfo = ts3Api.getChannelInfo(channel.channelId());
            var channelByNameExact = ts3Api.getChannelByNameExact(
                    channelInfo.getName(),
                    false
            );
            return channelByNameExact != null;
        }).collect(Collectors.toList());
    }

    private void loadChannelGroupsFromConfig() {
        var groupConfiguration = channelGroupsFile.loadObject(
                ChannelGroupConfiguration.class,
                ChannelGroupConfiguration.createWithDefaults()
        );
        channelGroups.addAll(groupConfiguration.channelGroups());
    }

    private void loadChannelsFromCacheFile() {
        var channelCacheFile = channelsCacheFile.loadObject(
                ChannelCacheFile.class,
                ChannelCacheFile.createDefault()
        );
        channelList.addAll(channelCacheFile.channels());
    }

    private void startChannelAmountChecker(Logger logger) {
        Scheduler.scheduleAtFixedRate(
                ChannelCheckRunnable.create(this, logger),
                0,
                100,
                TimeUnit.MILLISECONDS
        );
    }

    private void saveChannelsToFileCache() {
        channelsCacheFile.save(ChannelCacheFile.create(channelList));
    }

    public static ChannelRepository create(Logger logger, ChannelCreator channelCreator) {
        return new ChannelRepository(logger, channelCreator);
    }
}