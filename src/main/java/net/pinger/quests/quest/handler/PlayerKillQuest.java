package net.pinger.quests.quest.handler;

import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.quest.Quest;
import net.pinger.quests.quest.data.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerKillQuest extends QuestHandler {
    private final String playerName;

    public PlayerKillQuest(PlayerQuestsPlugin plugin, Quest quest) {
        super(plugin, quest);

        final PlayerData data = quest.getData();
        this.playerName = data.getPlayerName();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        final Player player = event.getEntity();
        if (player.getKiller() == null) {
            return;
        }

        final Player killer = player.getKiller();
        if (this.playerName == null) {
            this.handle(killer, (value) -> value + 1);
            return;
        }

        if (!player.getName().equalsIgnoreCase(this.playerName)) {
            return;
        }

        this.handle(killer, (value) -> value + 1);
    }
}
