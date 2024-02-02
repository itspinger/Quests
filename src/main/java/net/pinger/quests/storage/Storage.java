package net.pinger.quests.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.player.QuestPlayer;
import net.pinger.quests.quest.Quest;
import net.pinger.quests.reward.QuestReward;
import net.pinger.quests.storage.credentials.StorageCredentials;
import net.pinger.quests.storage.implementation.StorageImplementation;
import net.pinger.quests.storage.implementation.sql.SqlStorage;

public class Storage {
    private static final ScheduledExecutorService STORAGE_EXECUTOR = Executors.newScheduledThreadPool(2);

    private final HikariDataSource source;
    private final StorageImplementation implementation;

    /**
     * This constructor connects to the database with the given {@link StorageCredentials} credentials
     *
     * @param credentials the credentials
     * @param plugin the plugin
     */

    public Storage(PlayerQuestsPlugin plugin, StorageCredentials credentials) throws Exception {
        final HikariConfig hikari = new HikariConfig();

        // Set the jdbc url
        final String jdbc = "jdbc:mysql://" + credentials.getHost() + "?useSSL=false&useUnicode=yes&characterEncoding=UTF-8&serverTimezone=" + TimeZone.getDefault().getID();
        hikari.setJdbcUrl(jdbc);

        // Set the credentials
        hikari.setUsername(credentials.getUsername());
        hikari.setPassword(credentials.getPassword());
        hikari.setConnectionTimeout(30 * 1000); // 60 seconds
        hikari.setKeepaliveTime(0);
        hikari.setMinimumIdle(10);
        hikari.setMaximumPoolSize(10);
        hikari.setMaxLifetime(1800000); // 5 minutes

        this.source = new HikariDataSource(hikari);
        try (final InputStream stream = plugin.getResource("database/storage_sql.sql")) {
            StorageLoader.init(this, stream);
        }

        this.implementation = new SqlStorage(plugin, this);
    }

    /**
     * This method returns a new connection fetched from
     * the pool.
     *
     * @return the connection
     * @throws SQLException if an error occurred while fetching
     */

    public Connection getConnection() throws SQLException {
        return this.source.getConnection();
    }

    private <T> CompletableFuture<T> future(Callable<T> supplier) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return supplier.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, Storage.STORAGE_EXECUTOR);
    }

    public CompletableFuture<Void> loadAllQuests() {
        return this.future(() -> {
            this.implementation.loadAllQuests();
            return null;
        });
    }

    public CompletableFuture<Void> saveQuest(Quest quest) {
        return this.future(() -> {
            this.implementation.saveQuest(quest);
            return null;
        });
    }

    public CompletableFuture<QuestPlayer> loadPlayer(UUID uniqueId) {
        return this.future(() -> this.implementation.loadPlayer(uniqueId));
    }

    public CompletableFuture<Void> savePlayer(QuestPlayer player) {
        return this.future(() -> {
            this.implementation.savePlayer(player);
            return null;
        });
    }

    public CompletableFuture<Void> saveReward(Quest quest, QuestReward reward) {
        return this.future(() -> {
            this.implementation.saveReward(quest, reward);
            return null;
        });
    }

    public CompletableFuture<Void> deleteReward(QuestReward reward) {
        return this.future(() -> {
            this.implementation.deleteReward(reward);
            return null;
        });
    }

    /**
     * This method shuts down the entire connection pool.
     * <p>
     * Once the source has been shutdown, we won't be able
     * to connect to the database anymore.
     */

    public void shutdown() {
        this.source.close();
    }

}
