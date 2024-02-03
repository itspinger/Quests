package net.pinger.quests.quest.handler;

import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.item.XMaterial;
import net.pinger.quests.quest.Quest;
import net.pinger.quests.quest.data.BlockData;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceQuest extends QuestHandler {
    private final Material material;
    private final int data;

    public PlaceQuest(PlayerQuestsPlugin playerQuestsPlugin, Quest quest) {
        super(playerQuestsPlugin, quest);

        final BlockData data = quest.getData();
        this.material = data.getMaterial() == null ? null : data.getMaterial().parseMaterial();
        this.data = data.getMaterial() == null ? 0 : data.getMaterial().getData();
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        if (this.material == null) { // If the material is null, we listen to any block break
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
