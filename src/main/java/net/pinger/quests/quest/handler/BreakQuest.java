package net.pinger.quests.quest.handler;

import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.quest.Quest;
import net.pinger.quests.quest.QuestHandler;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakQuest extends QuestHandler {
    private Material material;
    private int data;

    public BreakQuest(PlayerQuestsPlugin playerQuestsPlugin, Quest quest, int goal) {
        super(playerQuestsPlugin, quest, goal);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        final Player player = event.getPlayer();
        // If the material is null, we listen to any block break
        if (this.material == null) {
            this.handle(player, (value) -> value + 1);
            return;
        }

        final Block block = event.getBlock();
        if (block.getType() != this.material || (this.data != -1 && block.getData() != this.data)) {
            return;
        }

        this.handle(player, (value) -> value + 1);
    }
}
