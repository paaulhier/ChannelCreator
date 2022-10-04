package de.keeeks.channelcreator.channel;

import de.keeeks.channelcreator.exception.QueryNotConnectedException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ChannelCheckRunnable implements Runnable {
    private final ChannelRepository channelRepository;
    private final Logger logger;

    private long pauseUntil = -1;

    @Override
    public void run() {
        if (pauseUntil >= System.currentTimeMillis()) return;

        for (var channelGroup : channelRepository.channelGroups()) {
            var channels = channelRepository.channelsOfGroup(channelGroup);
            var minChannels = channelGroup.channelGroupSettings().minChannels();
            try {
                var emptyChannels = channelRepository.emptyChannels(channels);

                if (emptyChannels.size() != 1) {
                    var amountToCreate = minChannels - channels.size();

                /*logger.info("Creating %s channels of group %s".formatted(
                        amountToCreate,
                        channelGroup.groupName()
                ));*/
                }
            } catch (QueryNotConnectedException e) {
                logger.log(Level.SEVERE,
                        "Could not check the channel groups for enough channels. Retrying after 30 seconds",
                        e);
                pauseUntil = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(30);
            }
        }
    }

    public static ChannelCheckRunnable create(ChannelRepository channelRepository,
                                              Logger logger) {
        return new ChannelCheckRunnable(channelRepository, logger);
    }
}