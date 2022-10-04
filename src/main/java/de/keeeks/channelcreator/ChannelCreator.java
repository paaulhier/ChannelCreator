package de.keeeks.channelcreator;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.reconnect.ConnectionHandler;
import com.github.theholywaffle.teamspeak3.api.reconnect.ReconnectStrategy;
import de.keeeks.channelcreator.channel.ChannelRepository;
import de.keeeks.channelcreator.command.CommandRepository;
import de.keeeks.channelcreator.command.InternalStopCommand;
import de.keeeks.channelcreator.configuration.TeamSpeakConfiguration;
import de.keeeks.channelcreator.exception.QueryNotConnectedException;
import de.keeeks.channelcreator.file.JsonFile;
import de.keeeks.channelcreator.util.Scheduler;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.logging.Logger;

@Getter
@Accessors(fluent = true)
public final class ChannelCreator {
    private static final JsonFile configurationFile = new JsonFile("config", "config");
    private static final TeamSpeakConfiguration teamSpeakConfiguration = configurationFile.loadObject(
            TeamSpeakConfiguration.class,
            TeamSpeakConfiguration.createDefault()
    );

    private final CommandRepository commandRepository;
    private final ChannelRepository channelRepository;
    private final Logger logger;

    private TS3Query ts3Query;
    private TS3Api ts3Api;

    private ChannelCreator(Logger logger) {
        this.logger = logger;
        this.commandRepository = CommandRepository.create(logger);
        this.channelRepository = ChannelRepository.create(logger, this);
    }

    private void preLoad() {
        commandRepository.registerCommands(
                InternalStopCommand.create()
        );
    }

    private void load() {
        connectToTeamSpeakQuery();
    }

    private void disable() {
        if (ts3Api != null) ts3Api.logout();
        if (ts3Query != null) ts3Query.exit();
    }

    public TS3Api ts3Api() throws QueryNotConnectedException {
        if (ts3Api == null) throw new QueryNotConnectedException();
        return ts3Api;
    }

    private void connectToTeamSpeakQuery() {
        var ts3Config = new TS3Config();
        ts3Config.setConnectionHandler(new ConnectionHandler() {
            @Override
            public void onConnect(TS3Api api) {
                updateTs3ApiAndLogin(api);
                logger.info("Connected to teamspeak.");
            }

            @Override
            public void onDisconnect(TS3Query ts3Query) {
                updateTs3ApiAndLogin(null);
            }
        });
        ts3Config.setQueryPort(teamSpeakConfiguration.queryPort())
                .setHost(teamSpeakConfiguration.hostname())
                .setLoginCredentials(teamSpeakConfiguration.username(), teamSpeakConfiguration.password())
                .setReconnectStrategy(ReconnectStrategy.constantBackoff())
                .setFloodRate(TS3Query.FloodRate.UNLIMITED);
        this.ts3Query = new TS3Query(ts3Config);
        ts3Query.connect();
    }

    private void updateTs3ApiAndLogin(TS3Api ts3Api) {
        this.ts3Api = ts3Api;
        if (ts3Api == null) return;

        this.ts3Api.selectVirtualServerByPort(teamSpeakConfiguration.port());
        this.ts3Api.setNickname(teamSpeakConfiguration.nickname());
    }

    public static void bootstrap(Logger logger) {
        var channelCreator = new ChannelCreator(logger);
        Scheduler.execute(() -> {
            channelCreator.preLoad();
            channelCreator.load();
        });
        Runtime.getRuntime().addShutdownHook(new Thread(channelCreator::disable));
    }
}