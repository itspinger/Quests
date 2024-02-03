package net.pinger.quests.quest.handler;

import net.pinger.quests.PlayerQuestsPlugin;
import net.pinger.quests.quest.Quest;
import net.pinger.quests.quest.data.EntityData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityKillQuest extends QuestHandler {
    private final EntityType entityType;

    public EntityKillQuest(PlayerQuestsPlugin plugin, Quest quest) {
        super(plugin, quest);

        final EntityData data = quest.getData();
        this.entityType = data.getEntityType();
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        final LivingEntity entity = event.getEntity();
        if (entity.getKiller() == null) {
            return;
        }

        final Player killer = entity.getKiller();
        if (this.entityType == null) {
            this.handle(killer, (value) -> value + 1);
            return;
        }

        if (event.getEntityType() != this.entityType) {
            return;
        }

        this.handle(killer, (value) -> value + 1);
    }
}
