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
    private Quest activeQuest;

    public QuestPlayer(UUID id) {
        this.id = id;
        this.questsMap = new HashMap<>();
    }

    public UUID getId() {
        return this.id;
    }

    public Map<Quest, QuestProgress> getQuestsMap() {
        return this.questsMap;
    }

    public void modifyQuests(Consumer<Map<Quest, QuestProgress>> consumer) {
        consumer.accept(this.getQuestsMap());
    }

    public boolean isActiveQuest(Quest quest) {
        if (this.activeQuest == null) {
            return false;
        }

        return this.activeQuest.equals(quest);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.id);
    }
}
