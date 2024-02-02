package net.pinger.quests.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.player.QuestPlayer;
import net.pinger.quests.storage.Storage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerQuestManager {
    private final Map<UUID, QuestPlayer> userMap = new HashMap<>();
    private final PlayerQuestsPlugin plugin;
    private final Storage storage;

    public PlayerQuestManager(PlayerQuestsPlugin plugin) {
        this.plugin = plugin;
        this.storage = plugin.getStorage();
    }

    public void loadPlayer(UUID id) {
        final QuestPlayer player;
        try {
            player = this.storage.loadPlayer(id).get();
        } catch (Exception e) {
            this.plugin.getLogger().log(Level.SEVERE, "Failed to load user", e);
            return;
        }

        this.userMap.put(id, player);
    }

    public QuestPlayer getPlayer(UUID id) {
        if (this.userMap.containsKey(id)) {
            return this.userMap.get(id);
        }

        // This will be called when the server is restarted with /reload
        // So this won't be created for online players
        // We will take a lazy approach in creating this instead of having 100 connections for each player
        this.loadPlayer(id);
        return this.userMap.get(id);
    }

    public void savePlayer(UUID id) {
        final QuestPlayer player = this.userMap.get(id);
        if (player == null) {
            return;
        }

        this.storage.savePlayer(player).join();
        this.userMap.remove(id);
    }

}
