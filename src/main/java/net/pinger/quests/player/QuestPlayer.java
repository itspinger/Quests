package net.pinger.quests.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import net.pinger.quests.quest.Quest;
import net.pinger.quests.quest.QuestProgress;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class QuestPlayer {
    private final UUID id;
    private final Map<Quest, QuestProgress> questsMap;

    public QuestPlayer(UUID id) {
        this(id, new HashMap<>());
    }

    public QuestPlayer(UUID id, Map<Quest, QuestProgress> questsMap) {
        this.id = id;
        this.questsMap = questsMap;
    }

    public UUID getId() {
        return this.id;
    }

    public Map<Quest, QuestProgress> getQuestsMap() {
        return this.questsMap;
    }

    public QuestProgress getProgress(Quest quest) {
        return this.questsMap.compute(quest, ($, progress) -> {
            if (progress != null) {
                return progress;
            }

            return new QuestProgress(0, false, false);
        });
    }

    public void modifyQuests(Consumer<Map<Quest, QuestProgress>> consumer) {
        consumer.accept(this.getQuestsMap());
    }

    public boolean isActiveQuest(Quest quest) {
        return this.getProgress(quest).isActive();
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.id);
    }

    public String getName() {
        final Player player = this.getPlayer();
        if (player == null) {
            return null;
        }

        return player.getName();
    }
}
