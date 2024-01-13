package net.pinger.quests.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.player.QuestPlayer;

public class PlayerQuestManager {
    private final Map<UUID, QuestPlayer> userMap = new HashMap<>();
    private final PlayerQuestsPlugin playerQuestsPlugin;

    public PlayerQuestManager(PlayerQuestsPlugin playerQuestsPlugin) {
        this.playerQuestsPlugin = playerQuestsPlugin;
    }

    public QuestPlayer loadPlayer(UUID id) {
        // TODO: Load the user by the uuid from the database
        final QuestPlayer player = new QuestPlayer(id);
        this.userMap.putIfAbsent(id, player);
        return player;
    }

    public QuestPlayer getPlayer(UUID id) {
        return this.userMap.get(id);
    }

    public void savePlayer(UUID id) {

    }

    public void removePlayer(UUID id) {
        this.savePlayer(id);
        this.userMap.remove(id);
    }

}
