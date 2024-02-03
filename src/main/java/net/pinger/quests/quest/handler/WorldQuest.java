package net.pinger.quests.quest.handler;

import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.quest.Quest;
import net.pinger.quests.quest.data.WorldData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class WorldQuest extends QuestHandler {
    private final String worldName;

    public WorldQuest(PlayerQuestsPlugin plugin, Quest quest) {
        super(plugin, quest);

        final WorldData data = quest.getData();
        this.worldName = data.getWorldName();
    }

    @EventHandler
    public void onDeath(PlayerChangedWorldEvent event) {
        final Player player = event.getPlayer();
        if (this.worldName == null) {
            this.handle(player, (value) -> value + 1);
            return;
        }

        if (!player.getWorld().getName().equalsIgnoreCase(this.worldName)) {
            return;
        }

        this.handle(player, (value) -> value + 1);
    }

}
